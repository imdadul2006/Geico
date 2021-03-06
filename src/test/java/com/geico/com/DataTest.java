package com.geico.com;

import com.geico.com.objectRepo.*;
import common.Base;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utility.DataReader;

import java.io.IOException;

public class DataTest extends Base {

    WebDriver driver;

    @BeforeTest
    public void setUPOS() {
        driver = getLocalDriver("Windows", "chrome");
    }

    @BeforeMethod
    public void setUp() throws IOException {

        setUp("http://www.geico.com");
    }

    @Test(dataProvider = "data")
    public void dataReader(String firstName, String lastName, String street, String apt, String zip, String DOB) throws IOException, InterruptedException {
        if(firstName!=null); //confirm data is not null

        HomePage hp = new HomePage(driver);
        hp.getSubmit().click();

        Thread.sleep(2000);
        CustomerInformation ci = new CustomerInformation(driver);
        ci.customerInfo(firstName, lastName, street, apt, zip, DOB);
        ci.getNext().click();

        Thread.sleep(2000);
        vehiclePositiveTest();
        driverInfoTest();
        discountsandcontactinfo();
        quota();
    }

    @DataProvider(name = "data")
    public static Object[][] credentials() throws IOException {

        DataReader dr = new DataReader();
        //System.setProperty("webdriver.chrome.driver", "../Geico/data/us-500.xlsx");
        String [][] data = dr.fileReader1("../Geico/data/us-500.xlsx");
        return data;

    }

    @AfterTest
    public static void cleanUp(){
        cleanUp();
    }

    public void vehicleNegativeTest() throws InterruptedException {

        Thread.sleep(2000);
        VehicleSelection vs = new VehicleSelection(driver);
        vs.getAddNoNewCar().click();

        // Assert that clicking next button on this page will change the year in red
        Assert.assertEquals(redColor,getColor(vs.getYearLabel()));

        // Assert that clicking next button on this page will change How is this vehicle primarily used? color
        Assert.assertEquals(redColor,getColor(vs.getVehicleUsed()));

    }

    public void vehiclePositiveTest() throws InterruptedException {
        Thread.sleep(2000);
        String source = driver.getPageSource();
        if (source.contains(errorMSG))
            testPage = false;

        // Assert.assertEquals(testPage,test);
        //To do confirm the title of the page matches as expected.

        if (testPage) {
            Thread.sleep(2000);
            VehicleSelection vs = new VehicleSelection(driver);
            selectByVisibleText("2017",vs.getVehicleYearSelect());
            selectByVisibleText("Honda",vs.getVehicleMakeSelect());
            selectByVisibleText("Accord",vs.getVehicleModelSelect());
            selectByIndex(1,vs.getBodyStyleSelect());
            selectByIndex(1,vs.getAntiTheftDeviceSelect());
            ListOfString(vs.getVehicleOwner(),"Owned").click();
            ListOfString(vs.getPrimaryUse(),"Pleasure").click();
            selectByValue("6000",vs.getAnnualMileageSelect());
            vs.getAddNoNewCar().click();
        }
        else
            System.out.println(incorrectMsg);
        testPage = true;
    }


    public void driverInfoTest() throws InterruptedException {

        //To do confirm the title of the page matches as expected.

        Thread.sleep(2000);
        String source = driver.getPageSource();
        if (source.contains(errorMSG))
            testPage = false;

        //  Assert.assertEquals(testPage,test);

        if (testPage) {
            Thread.sleep(2000);
            CarDriver cr = new CarDriver(driver);
            selectByValue("S", cr.getMaritalStatusSelect());
            ListOfString(cr.getGender(), "Female").click();
            cr.getSsn().sendKeys("102125403");
            ListOfString(cr.getOwnOrRent(), "Own").click();
            selectByValue("N", cr.getHasInsuranceSelect());
            cr.getAgeFirstLicense().sendKeys("19");
            selectByValue("T", cr.getEducationSelect());
            selectByValue("07", cr.getEducationSelect());
            cr.getAddNoNewCar();

       /* url = driver.getCurrentUrl();
        Assert.assertEquals("driverhistory",url.contains("driverhistory"));
*/
            Thread.sleep(2000);
            cr.getAddNoNewCar();
        }
        else{
            System.out.println(incorrectMsg);
        }
        testPage = true;
    }


    public void discountsandcontactinfo () throws InterruptedException {
        Thread.sleep(2000);
        String source = driver.getPageSource();
        if (source.contains(errorMSG))
            testPage = false;
        // Assert.assertEquals(testPage,test);
        if (testPage) {


            DetailPage dp = new DetailPage(driver);
            dp.getSelectNo();
            dp.getEmail().sendKeys("ihoq@gmail.com");

            try {
                dp.getKeepOriginal().click();
            } catch (Exception e) {};

            dp.getSubmit();
        }
        else
            System.out.println(incorrectMsg);
        testPage = true;
    }

    public void quota() throws InterruptedException {
        Thread.sleep(2000);
        DetailPage dp = new DetailPage(driver);
        String url = driver.getCurrentUrl();
        String source = driver.getPageSource();
        if (source.contains(errorMSG))
            testPage = false;
        // Assert.assertEquals(testPage,test);
        if (testPage) {
            String expectedUrl = "https://auto-buy.geico.com/nb#/sale/coverage/gskmsi?id=";
            if (url.equalsIgnoreCase(expectedUrl)) {
                dp.getContinue1().click();
            }
        }
        else
            System.out.println(incorrectMsg);
        testPage = true;
    }
}
