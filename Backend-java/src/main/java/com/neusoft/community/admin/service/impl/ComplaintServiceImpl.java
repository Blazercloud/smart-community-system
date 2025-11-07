// ComplaintServiceImpl.java
package com.neusoft.community.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.community.admin.entity.Complaint;
import com.neusoft.community.admin.mapper.ComplaintMapper;
import com.neusoft.community.admin.service.ComplaintService;
import org.springframework.stereotype.Service;

@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {
}
