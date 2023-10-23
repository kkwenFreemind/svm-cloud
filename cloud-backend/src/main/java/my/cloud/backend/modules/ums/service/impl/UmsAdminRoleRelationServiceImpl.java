package my.cloud.backend.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import my.cloud.backend.modules.ums.mapper.UmsAdminRoleRelationMapper;
import my.cloud.backend.modules.ums.model.UmsAdminRoleRelation;
import my.cloud.backend.modules.ums.service.UmsAdminRoleRelationService;
import org.springframework.stereotype.Service;

/**
 * 管理員角色管理Service實現類
 */
@Service
public class UmsAdminRoleRelationServiceImpl extends ServiceImpl<UmsAdminRoleRelationMapper, UmsAdminRoleRelation> implements UmsAdminRoleRelationService {
}
