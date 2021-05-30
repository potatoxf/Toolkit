package pxf.toolkit.extension.office.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json文档注册器
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public class JsonDocumentFormatRegistry extends SimpleDocumentFormatRegistry {

  public JsonDocumentFormatRegistry(InputStream input) throws JSONException, IOException {
    readJsonArray(IOUtils.toString(input));
  }

  public JsonDocumentFormatRegistry(String source) throws JSONException {
    readJsonArray(source);
  }

  private void readJsonArray(String source) throws JSONException {
    JSONArray array = new JSONArray(source);
    for (int i = 0; i < array.length(); i++) {
      JSONObject jsonFormat = array.getJSONObject(i);
      DocumentFormat format = new DocumentFormat();
      format.setName(jsonFormat.getString("name"));
      format.setExtension(jsonFormat.getString("extension"));
      format.setMediaType(jsonFormat.getString("mediaType"));
      if (jsonFormat.has("inputFamily")) {
        format.setInputFamily(DocumentFamily.valueOf(jsonFormat.getString("inputFamily")));
      }
      if (jsonFormat.has("loadProperties")) {
        format.setLoadProperties(toJavaMap(jsonFormat.getJSONObject("loadProperties")));
      }
      if (jsonFormat.has("storePropertiesByFamily")) {
        JSONObject jsonStorePropertiesByFamily =
            jsonFormat.getJSONObject("storePropertiesByFamily");
        for (String key : JSONObject.getNames(jsonStorePropertiesByFamily)) {
          Map<String, ?> storeProperties =
              toJavaMap(jsonStorePropertiesByFamily.getJSONObject(key));
          format.setStoreProperties(DocumentFamily.valueOf(key), storeProperties);
        }
      }
      addFormat(format);
    }
  }

  private Map<String, ?> toJavaMap(JSONObject jsonMap) throws JSONException {
    Map<String, Object> map = new HashMap<>();
    for (String key : JSONObject.getNames(jsonMap)) {
      Object value = jsonMap.get(key);
      if (value instanceof JSONObject) {
        map.put(key, toJavaMap((JSONObject) value));
      } else {
        map.put(key, value);
      }
    }
    return map;
  }
}
