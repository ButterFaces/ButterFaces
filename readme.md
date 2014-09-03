# ButterFaces

This component framework is a lightweight bootstrap compatible JSF2 component library. It enables most of the HTML5 features for default JSF components. 
In Addition to that it allows to activate tooltips and marks input fields if an validation error occurs.

![Foo](https://bytebucket.org/larmicBB/butterfaces/raw/d82deb4e4f30a57caf9274b7a0cdb04c1099aaf3/showcase.png)

A showcase is available under [www.butterfaces.org/showcase/](http://www.butterfaces.org/showcase/).

## Install

larmic-components is accessible by maven central repository. Add following dependency to use it

```xml
<dependency>
    <groupId>de.larmic.butterfaces</groupId>
	<artifactId>components</artifactId>
	<version>1.5.6</version>
</dependency>
```

For further versions (< 1.5.6) use

```xml
<dependency>
    <groupId>de.larmic</groupId>
	<artifactId>jsf2-components</artifactId>
	<version>1.5.5</version>
</dependency>
```

Or you can clone git repository and install maven plugin by yourself.

* Checkout the source: `git clone git@bitbucket.org:larmicBB/ButterFaces.git`.
* Use maven to install it `maven install`

## Getting started

Add `http://butterfaces.larmic.de/components"` (version further than 1.5.6 use `xmlns:l="http://larmic.de/jsf2"`) to your `<html .../>` and use `<!DOCTYPE html>` to activate html 5 features.

### Example

```xml
<!DOCTYPE html>
<html lang="en"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:l="http://butterfaces.larmic.de/components"
      xmlns:f="http://java.sun.com/jsf/core">
      ...
    <l:text id="test" ... />
</html>
```