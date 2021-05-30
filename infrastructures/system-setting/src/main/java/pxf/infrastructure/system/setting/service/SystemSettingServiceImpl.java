package pxf.infrastructure.system.setting.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pxf.infrastructure.system.setting.entity.SystemSetting;
import pxf.infrastructure.system.setting.mapper.SystemSettingMapper;

/**
 * {@code SystemSetting}ServiceImpl
 *
 * @author potatoxf
 * @date 2021/5/28
 */
public class SystemSettingServiceImpl extends ServiceImpl<SystemSettingMapper, SystemSetting>
    implements SystemSettingService {}
