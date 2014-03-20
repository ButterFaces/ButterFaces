# larmic-components

This component framework is a lightweight JSF2 component library. It enables most of the HTML5 features for default JSF components. 
In Addition to that it allows to activate tooltips and marks input fields if an validation error occurs.

![Foo](https://bytebucket.org/larmicBB/larmic-jsf2-components/raw/aec73eb8409af515797f05011a6e0ea6f741481f/showcase.png)

## Install

larmic-components is accessible by maven central repository. Add following dependency to use it

```xml
<dependency>
    <groupId>de.larmic</groupId>
	<artifactId>jsf2-components</artifactId>
	<version>1.4.1</version>
</dependency>
```

Or you can clone git repository and install maven plugin by yourself.

* Checkout the source: `git clone git@bitbucket.org:larmicBB/larmic-jsf2-components.git`.
* Use maven to install it `maven install`

## Getting started

Add `xmlns:l="http://larmic.de/jsf2"` to your `<html .../>` and use `<!DOCTYPE html>` to activate html 5 features.

### Example

```xml
<!DOCTYPE html>
<html lang="en"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:l="http://larmic.de/jsf2"
      xmlns:f="http://java.sun.com/jsf/core">
      ...
</html>
```