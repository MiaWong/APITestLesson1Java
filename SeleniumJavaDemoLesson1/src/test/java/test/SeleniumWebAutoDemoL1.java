package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.PageUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SeleniumWebAutoDemoL1 {
    WebDriver webDriver = null;

    PageUtil pageUtil = new PageUtil();

    @Before
    public void setUp() throws Exception {
        //开启Chrome浏览器
        webDriver = pageUtil.getChromeDriver();
    }

    /**
     * 在百度上查询，并点击进去查询结果第一条，页面切换
     */
    @Test
    public void openPageAndSrch()
    {
        //-------1. 打开Chrom并打开我们输入URL的网址
        pageUtil.openUrl(webDriver, "http://www.baidu.com");

        //窗口最大化
        webDriver.manage().window().maximize();

        //------2. 在百度上做查询
        WebElement txtInput = webDriver.findElement(By.id("kw"));
        txtInput.sendKeys("Selenium");

        WebElement searchBtn = webDriver.findElement(By.id("su"));
        searchBtn.click();

        //等待5秒，页面加载
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //------3.跳转到第一个查询结果的页面
        //拿到页面上所有的搜索结果
        List<WebElement> searchResults = webDriver.findElements(By.className("c-container"));

        //取第一个,并点击进去
        searchResults.get(0).findElement(By.tagName("a")).click();

        //------4.切换到另外一个页签
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //首先获得当前标签页
        String currentPageHandle = webDriver.getWindowHandle();

        //获得所有标签页
        Set<String> totalPageHandle = webDriver.getWindowHandles();

        //切换到另外一个页签
        for (String handleStr : totalPageHandle){
            if (!handleStr.equals(currentPageHandle)){
                webDriver.switchTo().window(handleStr);
            }
        }
    }

    //登录的web automation
    //对的用户名错误的密码
    @Test
    public void loginTestNullUsernameAndPwd()
    {
        openLinkAndLogin("", "", "必须填写内容");
    }

    //登录的web automation
    //对的用户名错误的密码
    @Test
    public void loginTestWrongUsernameOrPwd()
    {
        openLinkAndLogin("15111491478", "123456", "帐号不存在或密码错误！");
    }

    //登录的web automation
    //错误的用户名错误的密码
    @Test
    public void loginTestWrongUsernameAndPwd()
    {
        openLinkAndLogin("151114914", "123456", "帐号不存在或密码错误！");
    }

    //登录的web automation
    //正确的用户名和密码
    @Test
    public void loginTestRightCase()
    {
        openLinkAndLogin("15111491478", "mia123456", null);
    }

    /**
     *
     * @param usernameStr
     * @param pwdStr
     * @param errorMsg
     */
    private void openLinkAndLogin(String usernameStr, String pwdStr, String errorMsg) {
        //打开Chrom并打开我们输入URL的网址
        pageUtil.openUrl(webDriver, "http://www.tmkoo.com/");

        //---------1、去登录页面
        WebElement loginLink = webDriver.findElement(By.id("username"));
        loginLink.click();

        //等待5秒，页面加载
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //找到用户名输入框，输入用户名
        WebElement username = webDriver.findElement(By.id("j_username"));
        username.clear();
        username.sendKeys(usernameStr);

        //找到密码输入框，输入密码
        WebElement pwd = webDriver.findElement(By.id("j_password"));
        pwd.clear();
        pwd.sendKeys(pwdStr);

        //点击登录按钮
        WebElement submitBtn = webDriver.findElement(By.tagName("button"));
        submitBtn.click();

        //等待5秒，页面加载
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //用户名或密码错误，错误的case
        if (errorMsg != null) {
            WebElement error = webDriver.findElement(By.id("msg_error"));

            if((error != null) && error.getAttribute("style").toLowerCase().contains("red"))
            {
                //运行成功
            }
            else
            {
                Assert.fail("期望出现错误提示但是 ：" +
                        (error==null? "错误语句元素在页面不存在":"错误元素存在，颜色显示不正确."));
            }

        }
        //正确的用户名密码，应该跳转到搜索页面，可以做搜索
        else {
            //----------2、查询商标名
            WebElement inputTxt = webDriver.findElement(By.id("keywordT"));
            inputTxt.sendKeys("动脑");

            WebElement searchBtn = webDriver.findElement(By.tagName("button"));
            searchBtn.click();

        }
    }

    /*
    @After
    public void tearDown() throws Exception {
        if(webDriver != null)
        {
            webDriver.quit();
        }
    }
    */
}
