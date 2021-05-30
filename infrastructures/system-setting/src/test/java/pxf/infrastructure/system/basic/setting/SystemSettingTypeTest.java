package pxf.infrastructure.system.basic.setting;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pxf.infrastructure.system.setting.SettingType;

/**
 * SettingType Tester.
 *
 * @author <Authors name>
 * @version 1.0
 *     <pre>3月 14, 2021</pre>
 */
public class SystemSettingTypeTest {

  @Before
  public void before() throws Exception {}

  @After
  public void after() throws Exception {}

  @Test
  public void testMain() throws Exception {
    String number = "123";
    String numbers = "1,2,3,4,5,6";
    System.out.println(SettingType.INTEGER.parseValue(number));
    System.out.println(Arrays.toString(SettingType.INTEGER.parseValues(numbers)));
    System.out.println(SettingType.LONG.parseValue(number));
    System.out.println(Arrays.toString(SettingType.LONG.parseValues(numbers)));
    System.out.println(SettingType.DOUBLE.parseValue(number));
    System.out.println(Arrays.toString(SettingType.DOUBLE.parseValues(numbers)));
    System.out.println(SettingType.STRING.parseValue(number));
    System.out.println(Arrays.toString(SettingType.STRING.parseValues(numbers)));
    String b = "T";
    String bs = "1,2,3,4,5,6,T,F,true,false";
    System.out.println(SettingType.BOOLEAN.parseValue(b));
    System.out.println(Arrays.toString(SettingType.BOOLEAN.parseValues(bs)));
    String date = "2020-12-06";
    String dates = "2020-12-06,2020-12-08";
    System.out.println(SettingType.DATE.parseValue(date));
    System.out.println(Arrays.toString(SettingType.DATE.parseValues(dates)));
    String time = "2020-12-06T11:24:54";
    String times = "2020-12-06T11:24:54,2020-12-06T17:24:54";
    System.out.println(SettingType.DATETIME.parseValue(time));
    System.out.println(Arrays.toString(SettingType.DATETIME.parseValues(times)));
    String dateRange = "2020-12-06#2020-12-09";
    String dateRanges = "2020-12-06#2020-12-09,2020-12-08#2020-12-09";
    System.out.println(SettingType.DATE_RANGE.parseValue(dateRange));
    System.out.println(Arrays.toString(SettingType.DATE_RANGE.parseValues(dateRanges)));
    String timeRange = "2020-12-06T12:24:54#2020-12-09T12:24:54";
    String timeRanges =
        "2020-12-06T12:24:54#2020-12-09T12:24:54,2020-12-06T17:24:54#2020-12-09T12:24:54";
    System.out.println(SettingType.DATETIME_RANGE.parseValue(timeRange));
    System.out.println(Arrays.toString(SettingType.DATETIME_RANGE.parseValues(timeRanges)));
  }
}
