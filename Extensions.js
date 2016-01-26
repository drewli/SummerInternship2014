Selenium.prototype.doClickIfPresent = function(locator)
{
  if (selenium.isElementPresent(locator) == true)
    this.page().findElement(locator).click();
}

Selenium.prototype.doErrorIfElementPresent = function(locator, text)
{
  if (selenium.isElementPresent(locator) == true)
    throw new SeleniumError("\n-----------------------------------------------------------------\n"
                    + text + 
                    "\n-----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementNotPresent = function(locator, text)
{
  if (selenium.isElementPresent(locator) == false)
    throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text + 
                    "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementNotText = function(locator, text)
{
  while (text.indexOf(" |") >= 0 || text.indexOf("| ") >= 0) 
  {
    text = text.replace(" |", "|");
    text = text.replace("| ", "|");
  }
  if (selenium.getText(locator) != text.substring(0, text.indexOf("|")))
        throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text.substring(text.indexOf("|") + 1) +
                        "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementText = function(locator, text)
{
  while (text.indexOf(" |") >= 0 || text.indexOf("| ") >= 0) 
  {
    text = text.replace(" |", "|");
    text = text.replace("| ", "|");
  }
  if (selenium.getText(locator) == text.substring(0, text.indexOf("|")))
        throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text.substring(text.indexOf("|") + 1) +
                        "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementNotValue = function(locator, text)
{
  while (text.indexOf(" |") >= 0 || text.indexOf("| ") >= 0) 
  {
    text = text.replace(" |", "|");
    text = text.replace("| ", "|");
  }
  if (selenium.getValue(locator) != text.substring(0, text.indexOf("|")))
        throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text.substring(text.indexOf("|") + 1) +
                        "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementValue = function(locator, text)
{
  while (text.indexOf(" |") >= 0 || text.indexOf("| ") >= 0) 
  {
    text = text.replace(" |", "|");
    text = text.replace("| ", "|");
  }
  if (selenium.getValue(locator) == text.substring(0, text.indexOf("|")))
        throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text.substring(text.indexOf("|") + 1) +
                        "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementNotSelectedLabel = function(selectLocator, text)
{
  while (text.indexOf(" |") >= 0 || text.indexOf("| ") >= 0) 
  {
    text = text.replace(" |", "|");
    text = text.replace("| ", "|");
  }
  if (selenium.getSelectedLabel(selectLocator) != text.substring(0, text.indexOf("|")))
        throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text.substring(text.indexOf("|") + 1) +
                        "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doErrorIfElementSelectedLabel = function(selectLocator, text)
{
  while (text.indexOf(" |") >= 0 || text.indexOf("| ") >= 0) 
  {
    text = text.replace(" |", "|");
    text = text.replace("| ", "|");
  }
  if (selenium.getSelectedLabel(selectLocator) == text.substring(0, text.indexOf("|")))
        throw new SeleniumError("\n----------------------------------------------------------------\n"
                    + text.substring(text.indexOf("|") + 1) +
                        "\n----------------------------------------------------------------\n");
}

Selenium.prototype.doClickAndConfirm = function(locator)
{
  this.page().findElement(locator).click();
  while (selenium.isConfirmationPresent())
    selenium.getConfirmation();
}

Selenium.prototype.doClickIfPresentAndConfirm = function(locator)
{
  if (selenium.isElementPresent(locator) == true)
    this.page().findElement(locator).click();
  while (selenium.isConfirmationPresent())
    selenium.getConfirmation();
}

Selenium.prototype.doClickElementConditionally = function(locator)
{
  while (locator.indexOf(" |") >= 0 || locator.indexOf("| ") >= 0) 
  {
    locator = locator.replace(" |", "|");
    locator = locator.replace("| ", "|");
  }
  locator1 = locator.substring(0, locator.indexOf("|"));
  locator2 = locator.substring(locator.indexOf("|") + 1,
    locator.lastIndexOf("|"));
  locator3 = locator.substring(locator.lastIndexOf("|") + 1);
  if (selenium.isElementPresent(locator1) == true) 
  {
    this.page().findElement(locator2).click();
  }
  else
  {
    this.page().findElement(locator3).click();
  }
}

Selenium.prototype.doChooseElementToClickConditionally = function(conditionalLocator, locator)
{
  while (locator.indexOf(" |") >= 0 || locator.indexOf("| ") >= 0) 
  {
    locator = locator.replace(" |", "|");
    locator = locator.replace("| ", "|");
  }
  locator1 = locator.substring(0, locator.indexOf("|"));
  locator2 = locator.substring(locator.indexOf("|") + 1);
  if (selenium.isElementPresent(conditionalLocator) == true) 
  {
    this.page().findElement(locator1).click();
  }
  else
  {
    this.page().findElement(locator2).click();
  }
}

Selenium.prototype.doClickThenErrorIfConfirmationPresent = function(locator, text) 
{
  this.page().findElement(locator).click();
  if (selenium.isConfirmationPresent())
  {
    throw new SeleniumError("\n----------------------------------------------------------------\n"
      + text + "\n----------------------------------------------------------------\n");
  }
}

Selenium.prototype.doClickThenErrorIfConfirmationNotPresent = function(locator, text) 
{
  this.page().findElement(locator).click();
  if (selenium.isConfirmationPresent() == false)
  {
    throw new SeleniumError("\n----------------------------------------------------------------\n"
      + text + "\n----------------------------------------------------------------\n");
  }
  else
  {
    do 
    {
      selenium.getConfirmation();
    }
    while (selenium.isConfirmationPresent());
  }
}

Selenium.prototype.doClickThisAlways = function(locator)
{
  while (true)
  {
    this.page().findElement(locator).click();
  }
}