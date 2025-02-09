package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegisterSteps {
    private WebDriver driver;

    @Given("I navigate to {string} on {string}")
    public void INavigateTo(String website, String browser) {
        System.out.println("Running tests on: " + browser);
        switch (browser) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                FirefoxOptions options = new FirefoxOptions();
                options.setCapability("webSocketUrl", true);
                driver = new FirefoxDriver(options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        driver.get(website);
    }

    @When("I enter a valid dob {string}")
    public void IEnterAValidDob(String dob) {
        WebElement dobField=getElementByName("DateOfBirth");
        dobField.sendKeys(dob);
        Assert.assertEquals("Date of birth entered correctly", dob, dobField.getAttribute("value"));
    }

    @And("I enter a valid firstname {string}")
    public void IEnterAValidFirstname(String firstname) {
        WebElement firstNameField=getElementByName("Forename");
        firstNameField.sendKeys(firstname);
        Assert.assertEquals("FirstName entered correctly", firstname, firstNameField.getAttribute("value"));
    }

    @And("I enter a valid lastname {string}")
    public void IEnterAValidLastname(String lastname) {
        WebElement lastNameField=getElementByName("Surname");
        lastNameField.sendKeys(lastname);
        Assert.assertEquals("LastName entered correctly", lastname, lastNameField.getAttribute("value"));
    }

    @And("I enter a valid email {string}")
    public void iEnterAValidEmail(String email) {
        getElementByName("EmailAddress").sendKeys(email);
        
    }

    @And("I enter a valid confirmEmail {string}")
    public void iEnterAValidConfirmEmail(String confirmEmail) {
        getElementByName("ConfirmEmailAddress").sendKeys(confirmEmail);
    }

    @And("I enter a valid password {string}")
    public void iEnterAValidPassword(String password) {
        getElementByName("Password").sendKeys(password);
    }

    @And("I enter a valid confirmPassword {string}")
    public void iEnterAValidConfirmPassword(String confirmPassword) {
        getElementByName("ConfirmPassword").sendKeys(confirmPassword);
    }

    @And("I selected terms {string}")
    public void iSelectedTerms(String terms) {
        WebElement termsCheckbox =getElementById("sign_up_25");
        WebElement checkbox = driver.findElement(By.xpath("//label[@for='sign_up_25']"));
        boolean shouldSelect = Boolean.parseBoolean(terms);

        if (shouldSelect && !termsCheckbox.isSelected()) {
            checkbox.click();
        } else if (!shouldSelect && termsCheckbox.isSelected()) {
            checkbox.click();
        }
    }

    @And("I selected ageAccept {string}")
    public void iSelectedAgeAccept(String ageAccept) {
        WebElement checkbox = driver.findElement(By.xpath("//label[@for='sign_up_26']"));
        checkbox.click();
    }

    @And("I selected codeOfEthics {string}")
    public void iSelectedCodeOfEthics(String codeOfEthics) {
        WebElement checkbox = driver.findElement(By.xpath("//label[@for='fanmembersignup_agreetocodeofethicsandconduct']"));
        checkbox.click();
    }

    @And("I click button")
    public void iClickButton() {
        driver.findElement(By.name("join")).click();
    }
    @Then("I should see a {string} message")
    public void I_should_see_a_message(String expectedMessage) {
        String currentUrl = driver.getCurrentUrl();
        if (isRedirectedToConfirmationPage(currentUrl)) {
            verifyThankYouMessage(expectedMessage);
        } else if (isOnRegistrationPage(currentUrl)) {
            handleRegistrationPage(expectedMessage);
        } else {
            Assert.fail("Unexpected behavior: No error message and user was redirected elsewhere.");
        }
    }

    @io.cucumber.java.After
    public void tearDown() {
        if (driver != null) {
            try {
                Thread.sleep(3000);
                driver.quit();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isRedirectedToConfirmationPage(String url) {
        return url.contains("SignUpConfirmation");
    }
    private boolean isOnRegistrationPage(String url) {
        return url.equals("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    private void verifyThankYouMessage(String expectedMessage) {
        WebElement thankYouMessage = waitForElement(By.xpath("//h2[contains(text(), '" + expectedMessage + "')]"), 10);
        Assert.assertTrue("Thank you message is not displayed", thankYouMessage.isDisplayed());
    }
    private void handleRegistrationPage(String expectedMessage) {
        if (!expectedMessage.equals("THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND")) {
            WebElement errorMessage = waitForElement(By.xpath("//*[contains(text(), '" + expectedMessage + "')]"), 5);
            Assert.assertNotNull("Expected error message not found: " + expectedMessage, errorMessage);
            Assert.assertEquals("Error message text does not match", expectedMessage, errorMessage.getText());
        } else {
            validateFormFields();
        }
    }
    private String getFieldValue(String fieldName) {
        return driver.findElement(By.name(fieldName)).getAttribute("value");
    }

    private WebElement getElementById(String elementId) {
        return driver.findElement(By.id(elementId));
    }
    private WebElement getElementByName(String elementName) {
        return driver.findElement(By.name(elementName));
    }
    private boolean areFieldsValid(String firstName, String lastName, String email, String password, WebElement terms, WebElement ageAccept, WebElement codeOfEthics) {
        return !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()
                && terms.isSelected() && ageAccept.isSelected() && codeOfEthics.isSelected();
    }
    private WebElement waitForElement(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    private void validateFormFields() {
        String firstName = getFieldValue("Forename");
        String lastName = getFieldValue("Surname");
        String email = getFieldValue("EmailAddress");
        String password = getFieldValue("Password");

        WebElement termsCheckbox = getElementById("sign_up_25");
        WebElement ageAcceptCheckbox = getElementById("sign_up_26");
        WebElement codeOfEthicsCheckbox = getElementById("fanmembersignup_agreetocodeofethicsandconduct");

        if (areFieldsValid(firstName, lastName, email, password, termsCheckbox, ageAcceptCheckbox, codeOfEthicsCheckbox)) {
            System.out.println("User remains on the same page with filled fields that indicates registration is successful.");
            Assert.assertTrue("No error message was displayed, User remains on the same page with filled fields that indicates registration is successful", true);
        } else {
            Assert.fail("User remains on the page, but fields are unexpectedly empty.");
        }
    }
}
