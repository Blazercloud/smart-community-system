<template>
	<div class="service-container">
		<el-card>
			<template #header>
				<div class="card-header">
					<h3>便民服务</h3>
				</div>
			</template>

			<el-tabs v-model="activeTab">
				<el-tab-pane label="车位申请" name="parking">
					<div class="parking-panel">
						<el-row :gutter="20">
							<el-col :span="12">
								<el-card>
									<p>当前车位状态：</p>
									<el-descriptions column="1">
										<el-descriptions-item label="总车位">{{ parkingInfo.total || '-' }}</el-descriptions-item>
										<el-descriptions-item label="已绑定">{{ parkingInfo.bound || '-' }}</el-descriptions-item>
										<el-descriptions-item label="可用">{{ parkingInfo.available || '-' }}</el-descriptions-item>
									</el-descriptions>
								</el-card>
							</el-col>

							<el-col :span="12">
								<el-card>
									<el-form :model="parkingForm" :rules="parkingRules" ref="parkingFormRef" label-width="120px">
										<el-form-item label="车牌号" prop="license">
											<el-input v-model="parkingForm.license" placeholder="例如：辽A·12345" />
										</el-form-item>
										<el-form-item>
											<el-button type="primary" @click="handleBindLicense">申请绑定车位</el-button>
											<el-button @click="resetParkingForm">重置</el-button>
										</el-form-item>
									</el-form>
								</el-card>
							</el-col>
						</el-row>
					</div>
				</el-tab-pane>

				<el-tab-pane label="住户信息（出租情况）" name="resident">
					<div class="resident-panel">
						<el-card>
							<el-form :model="residentForm" :rules="residentRules" ref="residentFormRef" label-width="140px">
								<el-form-item label="姓名" prop="name">
									<el-input v-model="residentForm.name" />
								</el-form-item>

								<el-form-item label="手机号" prop="phone">
									<el-input v-model="residentForm.phone" />
								</el-form-item>

								<el-form-item label="是否承租（出租）" prop="isTenant">
									<el-switch v-model="residentForm.isTenant" active-text="是" inactive-text="否" />
								</el-form-item>

								<el-form-item label="房东姓名" prop="landlordName">
									<el-input v-model="residentForm.landlordName" :disabled="!residentForm.isTenant" />
								</el-form-item>

								<el-form-item label="房东电话" prop="landlordPhone">
									<el-input v-model="residentForm.landlordPhone" :disabled="!residentForm.isTenant" />
								</el-form-item>

								<el-form-item label="租期起始" prop="leaseStart">
									<el-date-picker v-model="residentForm.leaseStart" type="date" placeholder="开始日期" :disabled="!residentForm.isTenant" />
								</el-form-item>

								<el-form-item label="租期结束" prop="leaseEnd">
									<el-date-picker v-model="residentForm.leaseEnd" type="date" placeholder="结束日期" :disabled="!residentForm.isTenant" />
								</el-form-item>

								<el-form-item>
									<el-button type="primary" @click="saveResidentInfo">保存信息</el-button>
									<el-button @click="loadResidentInfo">从服务器刷新</el-button>
								</el-form-item>
							</el-form>
						</el-card>
					</div>
				</el-tab-pane>
			</el-tabs>
		</el-card>
	</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getParkingInfo, bindParkingLicense } from '@/api/community'
import { getUserInfo } from '@/api/user'

const activeTab = ref('parking')

// 停车相关
const parkingInfo = ref({})
const parkingForm = ref({ license: '' })
const parkingFormRef = ref(null)
const parkingRules = {
	license: [{ required: true, message: '请输入车牌号', trigger: 'blur' }]
}

// 住户信息
const residentForm = ref({
	name: '',
	phone: '',
	isTenant: false,
	landlordName: '',
	landlordPhone: '',
	leaseStart: null,
	leaseEnd: null
})
const residentFormRef = ref(null)
const residentRules = {
	name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
	phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

async function fetchParking() {
	try {
		const res = await getParkingInfo()
		parkingInfo.value = res.data || {}
	} catch (err) {
		ElMessage.error('获取车位信息失败')
	}
}

async function handleBindLicense() {
	if (!parkingFormRef.value) return
	parkingFormRef.value.validate(async (valid) => {
		if (!valid) return
		try {
			const res = await bindParkingLicense({ license: parkingForm.value.license })
			ElMessage.success(res?.data?.message || '申请已提交')
			fetchParking()
			parkingForm.value.license = ''
		} catch (err) {
			ElMessage.error(err?.message || '绑定失败')
		}
	})
}

function resetParkingForm() {
	if (parkingFormRef.value) parkingFormRef.value.resetFields()
}

async function loadResidentInfo() {
	try {
		const res = await getUserInfo()
		const info = res.data || {}
		residentForm.value.name = info.name || info.username || ''
		residentForm.value.phone = info.phone || ''
		// 如果后端返回租赁相关字段，可在此处映射
		const local = JSON.parse(localStorage.getItem('residentInfo') || '{}')
		Object.assign(residentForm.value, local)
		ElMessage.success('用户信息已刷新（含本地租赁信息）')
	} catch (err) {
		ElMessage.error('获取用户信息失败')
	}
}

function saveResidentInfo() {
	if (!residentFormRef.value) return
	residentFormRef.value.validate((valid) => {
		if (!valid) return
		// 暂时保存到 localStorage，后端未提供更新接口
		localStorage.setItem('residentInfo', JSON.stringify(residentForm.value))
		ElMessage.success('住户信息已保存（本地），后端未配置保存接口')
	})
}

onMounted(() => {
	fetchParking()
	loadResidentInfo()
})
</script>

<style scoped>
.service-page .card-header h3 {
	margin: 0;
}
.parking-panel, .resident-panel {
	margin-top: 12px;
}
</style>
