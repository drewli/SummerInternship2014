Selenium.prototype.doClickIfPresent = function(locator)
{
  if (selenium.isElementPresent(locator) == true)
    this.page().findElement(locator).click();
}

Selenium.prototype.doErrorIfElementPresent = function(locator, text)
{
  if (selenium.isElementPresent(locator) == true)
    throw new Error("\n-----------------------------------------------------------------\n"
                    + text + 
                    "\n-----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementNotPresent = function(locator, text)
{
  if (selenium.isElementPresent(locator) == false)
    throw new Error("\n----------------------------------------------------------------\n"
                    + text + 
                    "\n----------------------------------------------------------------\n");
}


