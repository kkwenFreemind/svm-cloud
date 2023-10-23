package my.cloud.backend.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import my.cloud.backend.modules.ums.mapper.UmsRoleResourceRelationMapper;
import my.cloud.backend.modules.ums.model.UmsRoleResourceRelation;
import my.cloud.backend.modules.ums.service.UmsRoleResourceRelationService;
import org.springframework.stereotype.Service;

/**
 * 角色資源關聯管理Service實現類
 */
@Service
public class UmsRoleResourceRelationServiceImpl extends ServiceImpl<UmsRoleResourceRelationMapper, UmsRoleResourceRelation> implements UmsRoleResourceRelationService {
}
