import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities

waiting {
    timeout = 5
}
driver = {
//    new PhantomJSDriver(new DesiredCapabilities())
    new ChromeDriver()

}
environments {
    // run as “grails -Dgeb.env=chrome test-app”
    // See: http://code.google.com/p/selenium/wiki/ChromeDriver
    phantomjs {
        driver = {
            new PhantomJSDriver(new DesiredCapabilities())
        }
    }

    chrome {
        driver = {
            new ChromeDriver()
        }
    }
}
//driver.manage().window().setSize(new Dimension(1280, 800))

reportsDir = "target/test-reports/geb"
baseUrl="http://localhost:8080/"