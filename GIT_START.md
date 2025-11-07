# **Gitee 团队安全 Git 开发流程**

## **1️⃣ 分支策略**

- `main`（或 `master`）：主分支，存放稳定代码，禁止直接 push
- `feature/xxx`：功能分支，每个任务/功能一个分支
- `bugfix/xxx`：修复分支，用于修复线上 bug
- `release/xxx`（可选）：发布分支，用于上线打包

> ⚠️ **建议开启 Gitee 保护分支**：禁止直接 push main 分支，只允许通过合并请求（Merge Request / Pull Request）合并。

------

## **2️⃣ 克隆仓库**

组员第一次开发：

```
git clone <仓库地址>
cd <仓库名>
git checkout main
git pull origin main
```

> 保证本地代码与远程 main 一致。

------

## **3️⃣ 创建个人开发分支**

```
git checkout -b feature/功能名
```

例如：

```
git checkout -b feature/login
```

------

## **4️⃣ 开发流程**

1. 在分支上开发功能
2. 每完成一部分代码进行 commit：

```
git add .
git commit -m "功能说明或改动说明"
```

1. 定期拉取远程 main 更新，保持分支最新：

```
git fetch origin
git rebase origin/main
```

> ⚠️ 使用 `rebase` 可保持提交历史整洁，如果不熟悉可以用 `merge`：`git merge origin/main`

------

## **5️⃣ 推送分支到远程**

```
git push origin feature/功能名
```

> 这样不会影响 main 分支，组员可以自由开发。

------

## **6️⃣ 提交合并请求（Merge Request）**

1. 登录 Gitee → 仓库 → “合并请求”
2. 选择 `feature/功能名` → 合并到 `main`
3. 由主分支负责人（或你）审核代码后合并
4. 避免直接 push main 分支

------

## **7️⃣ 更新本地主分支**

合并后，所有成员更新本地 main：

```
git checkout main
git pull origin main
```

------

## **8️⃣ 常见注意事项**

- **禁止直接在 main 上 push**
- **每次开发前先 pull 最新 main**
- **分支命名规范**，便于管理
- **解决冲突时**，小心保留正确版本
- **定期清理无用分支**，保持仓库整洁