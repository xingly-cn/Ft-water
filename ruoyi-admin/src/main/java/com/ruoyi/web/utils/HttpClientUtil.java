package com.ruoyi.web.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String doPost(String url, Map<String, String> params) {
        String charset = "utf-8";
        StringBuilder response = new StringBuilder();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // 设置Http Post数据
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        if (params != null) {
            Set<String> keySet = params.keySet();
            NameValuePair[] param = new NameValuePair[keySet.size()];
            int i = 0;
            for (String key : keySet) {
                param[i] = new NameValuePair(key, params.get(key));
                i++;
            }
            method.setRequestBody(param);
        }
        InputStream responseBodyStream = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                responseBodyStream = method.getResponseBodyAsStream();
                streamReader = new InputStreamReader(responseBodyStream, charset);
                reader = new BufferedReader(streamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        } finally {
            try {
                if (responseBodyStream != null) {
                    responseBodyStream.close();
                }
                if (streamReader != null) {
                    streamReader.close();
                }
                if (reader != null) {
                    reader.close();
                }

            } catch (IOException e) {
                logger.error("执行HTTP Post请求" + url + "时，发生异常，关闭流异常！", e);
                e.printStackTrace();
            }
            method.releaseConnection();
        }
        return response.toString();
    }

    public static byte[] doGetReturnData(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            return out.toByteArray();
        } catch (Exception e) {
            logger.error("http get出错", e);
        }
        return null;
    }

    public static String doPost(String url, String jsonData) {
        return doPost(url, jsonData, StandardCharsets.UTF_8);
    }

    public static String doPost(String url, String jsonData, Charset charset) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonData, "utf-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            String r = EntityUtils.toString(entity, charset);
            logger.info("result: {}", r);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ---  http客户端发送  json参数格式  ---
     *
     * @param url return java.lang.String
     * @author lf
     * @date 2019-06-22 11:43
     */
    public static Map sendPostJson(String url, Map paramMap) {
        Map map = null;
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)
                .setConnectionRequestTimeout(35000)// 设置连接主机服务超时时间
                .setSocketTimeout(60000) // 设置连接请求超时时间
                .build(); // 设置读取数据连接超时时间
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            JSONObject paramJson = new JSONObject(paramMap);
            StringEntity paramEntity = new StringEntity(paramJson.toString(), "UTF-8"); // 对参数进行编码
            paramEntity.setContentType("application/json; charset=utf-8");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(paramEntity);
            httpPost.setConfig(requestConfig);

            //调用接口
            httpResponse = httpClient.execute(httpPost);
            //获取返回值
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                String responseStr = EntityUtils.toString(entity, "UTF-8");
                logger.info("HttpClientUtil.sendPostJson() - responseStr <== " + responseStr);
                if (StringUtils.isEmpty(responseStr)) {
                    responseStr = "{}";
                }
                //获得相应状态码
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.info("RequestUtils - statusCode <== " + statusCode);
                if (HttpServletResponse.SC_OK == statusCode) {
                    JSONObject dataJson = (JSONObject) JSONObject.parse(responseStr);
                    map = new HashMap(dataJson);
                }
            }
        } catch (Exception e) {
            logger.error("sendPostJson调用外部接口异常：" + e);
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResponse(httpResponse, httpClient);
        }
        return map;
    }

    // 关闭资源
    private static void closeResponse(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        // 关闭资源
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            logger.error("http get出错", e);
        }
        return null;
    }

    public static InputStream doPostReturnInputStream(String url, String jsonData) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonData, "utf-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            response.getEntity().writeTo(data);
            return new ByteArrayInputStream(data.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ---  http客户端发送  json参数格式  ---
     *
     * @param url return java.lang.String
     * @author lf
     * @date 2019-06-22 11:43
     */
    public static Map sendPostJsonStr(String url, String jsonStr) {
        Map map = null;
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)// 设置连接主机服务超时时间
                .setSocketTimeout(6000) // 设置连接请求超时时间
                .build(); // 设置读取数据连接超时时间
        // 创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {

            StringEntity paramEntity = new StringEntity(jsonStr, "UTF-8"); // 对参数进行编码
            paramEntity.setContentType("application/json; charset=utf-8");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(paramEntity);
            httpPost.setConfig(requestConfig);

            //调用接口
            httpResponse = httpClient.execute(httpPost);
            //获取返回值
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                String responseStr = EntityUtils.toString(entity, "UTF-8");
                logger.info("HttpClientUtil.sendPostJson() - responseStr--------" + responseStr);
                if (StringUtils.isEmpty(responseStr)) {
                    responseStr = "{}";
                }
                //获得相应状态码
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                logger.info("RequestUtils - statusCode ------ " + statusCode);
                if (HttpServletResponse.SC_OK == statusCode) {
                    JSONObject dataJson = (JSONObject) JSONObject.parse(responseStr);
                    map = new HashMap(dataJson);
                }
            }
        } catch (Exception e) {
            logger.error("sendPostJson调用外部接口异常：" + e);
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResponse(httpResponse, httpClient);
        }
        return map;
    }

    public static String doPostObject(String url, Map<String, Object> params) {
        String charset = "utf-8";
        StringBuilder response = new StringBuilder();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // 设置Http Post数据
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        if (params != null) {
            Set<String> keySet = params.keySet();
            NameValuePair[] param = new NameValuePair[keySet.size()];
            int i = 0;
            for (String key : keySet) {
                param[i] = new NameValuePair(key, params.get(key).toString());
                i++;
            }
            method.setRequestBody(param);
        }
        InputStream responseBodyStream = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                responseBodyStream = method.getResponseBodyAsStream();
                streamReader = new InputStreamReader(responseBodyStream, charset);
                reader = new BufferedReader(streamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        } finally {
            try {
                if (responseBodyStream != null) {
                    responseBodyStream.close();
                }
                if (streamReader != null) {
                    streamReader.close();
                }
                if (reader != null) {
                    reader.close();
                }

            } catch (IOException e) {
                logger.error("执行HTTP Post请求" + url + "时，发生异常，关闭流异常！", e);
                e.printStackTrace();
            }
            method.releaseConnection();
        }
        return response.toString();
    }

//	public static String postFile(String url, File file, String type, String id, String code, String status)throws Exception{
//		//组装参数 param
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addBinaryBody("multipartFilesList", file);
//		builder.addTextBody("taskType",type);
//		builder.addTextBody("recordStatus",status);
//		builder.addTextBody("finishTime",String.valueOf(System.currentTimeMillis() / 1000));
//		builder.addTextBody("subTaskId",id);
//		builder.addTextBody("requestCode",code);
//		//设置浏览器兼容模式
//		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//		//编码
//		builder.setCharset(StandardCharsets.UTF_8);
//		return httpPost(null,builder.build(), url);
//	}

    public static String httpPost(Map<String, String> map, HttpEntity body, String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000).setSocketTimeout(3000).setConnectTimeout(3000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(body);
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getKey());
            }
        }
        //send
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        String result = null;

        try {
            response = httpClient.execute(httpPost);
            inputStream = response.getEntity().getContent();
            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("io 异常--", e);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
