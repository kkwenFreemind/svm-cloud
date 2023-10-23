package my.cloud.backend.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import my.cloud.backend.modules.ums.mapper.UmsRoleMenuRelationMapper;
import my.cloud.backend.modules.ums.model.UmsRoleMenuRelation;
import my.cloud.backend.modules.ums.service.UmsRoleMenuRelationService;
import org.springframework.stereotype.Service;

/**
 * 角色菜單關聯管理Service實現類
 */
@Service
public class UmsRoleMenuRelationServiceImpl extends ServiceImpl<UmsRoleMenuRelationMapper, UmsRoleMenuRelation> implements UmsRoleMenuRelationService {
}
