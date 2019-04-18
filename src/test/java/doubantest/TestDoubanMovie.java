package doubantest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 这个类是用来测试豆瓣电影搜索接口的
 * step1：发送http get请求，获得response
 * step2：结合搜索条件判断response是否符合预期
 * 期望结果：（因为我们没有电影搜索的需求文档，所以我们这里假设只要每个subject里面有我们的搜索条件，
 * 这个测试用例就通过）
 */
public class TestDoubanMovie {
    //搜索电影接口url
    public final String apiUrl = "https://api.douban.com/v2/movie/search";

    @Test
    //按演职人员搜索：https://api.douban.com/v2/movie/search?q=周星驰
    public void actTestcase() {
        String param = "q=周星驰";
        testMovieSearch( param);
    }

    @Test
    //按片名搜索：https://api.douban.com/v2/movie/search?q=大话西游
    public void nameTestcase() {
        String param = "q=大话西游";
        testMovieSearch( param);
    }

    @Test
    //按类型搜索：https://api.douban.com/v2/movie/search?tag=喜剧
    public void typeTestcase() {
        String param = "tag=喜剧";
        testMovieSearch( param);
    }

    /**
     * @param param testcase 传过来的搜索条件比如（"q=周星驰"，"tag=喜剧"）
     */
    private void testMovieSearch(String param)
    {
        String strResult = doGet(param);
        String eMsg = isResponseAsExpect(strResult, param);

        if (StringUtils.isNotEmpty(eMsg.trim())) {
            Assert.fail(eMsg);
        }
    }

    /**
     * step1：拼接参数得到请求url，用httpclient发get请求获得response
     * @param param
     * @return
     */
    private String doGet(String param) {
        //接口URL+搜索条件=request Url。
        // https://api.douban.com/v2/movie/search?tag=喜剧
        StringBuffer reqURL = new StringBuffer(apiUrl).append("?").append(param);
        String strResult = null;
        try {
            //搜索条件有中文，防止乱码，我们用字符编码集UTF-8来decode一下
            reqURL = new StringBuffer(URLDecoder.decode(reqURL.toString(), "UTF-8"));

            DefaultHttpClient httpClient = new DefaultHttpClient();

            //先创建一个HttpGet对象,传入目标的网络地址,然后调用HttpClient的execute()方法即可
            HttpGet getRequest = new HttpGet(reqURL.toString());

            //这一段代码是http header的设置
            getRequest.setHeader("accept", "*/*");
            getRequest.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            getRequest.setHeader("connection", "Keep-Alive");

            //执行execute()方法之后会返回一个HttpResponse对象,服务器所返回的所有信息就保护在HttpResponse里面.
            HttpResponse response = httpClient.execute(getRequest);

            //请求发送成功，并得到响应
            //先取出服务器返回的状态码,如果等于200就说明请求和响应都成功了:
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                strResult = EntityUtils.toString(response.getEntity());
            } else {
                Assert.fail("get请求提交失败:" + reqURL.toString());
            }
        } catch (IOException e) {
            Assert.fail("get请求提交失败:" + reqURL.toString() + e);
        }

        //打印response
//        System.out.println(strResult);

        return strResult;
    }

    /**
     *step2：结合搜索条件判断response是否符合预期
     *  期望结果：（因为我们没有电影搜索的需求文档，所以我们这里假设只要每个subject里面有我们的搜索条件，
     *  这个测试用例就通过）
     *
     * @param strResult 之前http请求返回的response
     * @param param  搜索条件，用来判断测试用例是否通过的条件
     * @return
     */
    //我们刚刚看到response是json格式的，我们要先把response String
    // 转换成jsonobject，然后从中取得subjects，我们刚刚看到subjects由很多电影object组成，
    // 所以把subjects转换成jsonarray，最后用这个jsonarray做遍历去判断每个电影object。
    private String isResponseAsExpect(String strResult, String param) {
        StringBuffer eMsg = new StringBuffer();

        String expValue = param.substring(param.indexOf('=') + 1);

        //把json字符串转换成json对象
        JSONObject jsonResult = JSONObject.fromObject(strResult);

        //把json对象中的movies对象
        JSONArray subjects = jsonResult.getJSONArray("subjects");

        JSONObject object = null;
        for (int i = 0; i < subjects.size(); i++) {
            object = subjects.getJSONObject(i);
            String sub = object.toString();

            if (!sub.contains(expValue)) {
                eMsg.append("\n").append("search parameter is : ").append(param)
                        .append("this movie should not in search result ").append(sub);
            }
        }

        return eMsg.toString();
    }
}