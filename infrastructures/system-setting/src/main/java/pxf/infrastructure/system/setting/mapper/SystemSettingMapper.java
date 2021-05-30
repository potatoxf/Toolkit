package pxf.infrastructure.system.setting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.setting.entity.SystemSetting;

/**
 * {@code SystemSetting}Mapper
 *
 * @author potatoxf
 * @date 2021/5/28
 */
@Mapper
public interface SystemSettingMapper extends BaseMapper<SystemSetting> {}
