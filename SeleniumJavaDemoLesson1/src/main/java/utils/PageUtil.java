package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class PageUtil {
    public WebDriver getChromeDriver()
    {
        // chromedriver服务地址
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // 新建一个WebDriver 的对象，new 的是Chrome的驱动
        WebDriver chromeDriver = new ChromeDriver();

        return chromeDriver;
    }

    public void openUrl(WebDriver webDriver, String url)
    {
        //打开我们输入URL的网址
        webDriver.get(url);

        //等待5秒，页面加载
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

}
