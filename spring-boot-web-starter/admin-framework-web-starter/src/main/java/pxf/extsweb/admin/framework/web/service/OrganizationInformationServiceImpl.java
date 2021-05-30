package pxf.extsweb.admin.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import pxf.extsweb.admin.framework.web.entity.AbstractHierarchicalEntity;
import pxf.extsweb.admin.framework.web.entity.OrganizationInformation;
import pxf.extsweb.admin.framework.web.mapper.OrganizationInformationMapper;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Service
public class OrganizationInformationServiceImpl
    extends ServiceImpl<OrganizationInformationMapper, OrganizationInformation>
    implements OrganizationInformationService {

  private final OrganizationInformationMapper organizationInformationMapper;

  public OrganizationInformationServiceImpl(
      OrganizationInformationMapper organizationInformationMapper) {
    this.organizationInformationMapper = organizationInformationMapper;
  }

  @Nonnull
  @Override
  public List<OrganizationInformation> buildOrganizationTree() {
    List<OrganizationInformation> organizationInformations =
        organizationInformationMapper.selectList(null);
    Collections.sort(organizationInformations);
    return AbstractHierarchicalEntity.buildTree(organizationInformations);
  }

  @Nonnull
  @Override
  public List<OrganizationInformation> findDepartmentByEmployeeId(Long id) {
    if (id == null) {
      return Collections.emptyList();
    }
    return findOrganizationByEmployeeIdAndType(id, OrganizationInformation.DEPARTMENT);
  }

  @Nonnull
  @Override
  public List<OrganizationInformation> findRoleByEmployeeId(Long id) {
    if (id == null) {
      return Collections.emptyList();
    }
    return findOrganizationByEmployeeIdAndType(id, OrganizationInformation.ROLE);
  }

  private List<OrganizationInformation> findOrganizationByEmployeeIdAndType(Long id, String type) {
    QueryWrapper<OrganizationInformation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("employee_id", id);
    queryWrapper.eq("type", type);
    return organizationInformationMapper.selectList(queryWrapper);
  }
}
