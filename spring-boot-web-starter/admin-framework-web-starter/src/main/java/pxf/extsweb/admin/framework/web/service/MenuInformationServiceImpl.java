package pxf.extsweb.admin.framework.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import pxf.extsweb.admin.framework.web.entity.AbstractHierarchicalEntity;
import pxf.extsweb.admin.framework.web.entity.MenuInformation;
import pxf.extsweb.admin.framework.web.mapper.MenuInformationMapper;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
@Service
public class MenuInformationServiceImpl extends ServiceImpl<MenuInformationMapper, MenuInformation>
    implements MenuInformationService {

  private final MenuInformationMapper menuInformationMapper;

  public MenuInformationServiceImpl(MenuInformationMapper menuInformationMapper) {
    this.menuInformationMapper = menuInformationMapper;
  }

  @Override
  public List<MenuInformation> buildMenuTree() {
    List<MenuInformation> menuInformationList = menuInformationMapper.selectList(null);
    Collections.sort(menuInformationList);
    return AbstractHierarchicalEntity.buildTree(menuInformationList);
  }

  @Override
  public List<MenuInformation> buildMenuTreeByEmployeeId(Long employeeId) {
    List<MenuInformation> menuInformationList = menuInformationMapper.selectList(null);
    Collections.sort(menuInformationList);
    return AbstractHierarchicalEntity.buildTree(menuInformationList);
  }
}
