package my.cloud.backend.modules.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import my.cloud.backend.modules.oms.mapper.OmsOrganizationMapper;
import my.cloud.backend.modules.oms.model.OmsOrganization;
import my.cloud.backend.modules.oms.service.OmsOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 單位組織架構Service實現類
 */
@Service
@Slf4j
public class OmsOrganizationServiceImpl extends ServiceImpl<OmsOrganizationMapper, OmsOrganization> implements OmsOrganizationService {

    @Autowired
    private OmsOrganizationMapper omsOrganizationMapper;

    @Override
    public boolean create(OmsOrganization organization) {
        organization.setCreateTime(new Date());
        updateLevel(organization);
        return save(organization);
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean success = removeByIds(ids);
        return success;
    }

    @Override
    public boolean delete(Long id) {

        boolean success = removeById(id);
        return success;
    }

    @Override
    public Page<OmsOrganization> list(Long parentId, Integer pageSize, Integer pageNum) {
        Page<OmsOrganization> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OmsOrganization> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<OmsOrganization> lambda = wrapper.lambda();

        lambda.eq(OmsOrganization::getParentId, parentId)
                .orderByDesc(OmsOrganization::getSort);

        return page(page, wrapper);


    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        OmsOrganization organization = new OmsOrganization();
        organization.setId(id);
        organization.setStatus(status);
        return updateById(organization);
    }

    @Override
    public boolean update(Long id, OmsOrganization organization) {
        organization.setId(id);
        boolean success = updateById(organization);
        return success;
    }




    /**
     * 修改單位層級
     */
    private void updateLevel(OmsOrganization omsOrganization) {
        if (omsOrganization.getParentId() == 0) {
            //没有父菜單時為一級菜單
            omsOrganization.setLevel(0);
        } else {
            //有父菜單時選擇根據父菜單level設置
            OmsOrganization parentOrganization = getById(omsOrganization.getParentId());
            if (parentOrganization != null) {
                omsOrganization.setLevel(parentOrganization.getLevel() + 1);
            } else {
                parentOrganization.setLevel(0);
            }
        }
    }

    @Override
    public List<OmsOrganization> OrganizationLowList(Long parentId){
        log.info("OrganizationLowList.......{}",parentId );
        return omsOrganizationMapper.getOrgListByParentId(parentId);
    }

    @Override
    public Page<OmsOrganization> list( Integer pageSize, Integer pageNum) {
        Page<OmsOrganization> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OmsOrganization> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<OmsOrganization> lambda = wrapper.lambda();

        return page(page, wrapper);
    }
}
