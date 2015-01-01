eclipse-addons
==============

I found e(fx)clipse ```core.di```, ```core.adapter```, ```core.text``` packages very good additions to Eclipse's own framework and then decided to factor out their code in an autonomous bundle (I apologize for the shameful package name): ```com.andrearichiardi.eclipse.addons```.

As a starting point, try [this](http://tomsondev.bestsolution.at/2013/11/21/writing-ieclipsecontext-less-code/) blog post and [this other](https://wiki.eclipse.org/Efxclipse/Runtime/Recipes#Publishing_to_the_IEclipseContext)
excerpt from the e(fx)clipse wiki.

This new packaging removes all the Java 8 type annotations and adds [retrolambda](https://github.com/orfjackal/retrolambda) to backport the new constructs.
The Execution Environment has been set to ```JavaSE-1.6```. Kepler and E4 (Luna and higher) are supported.

Download
--------

The artifacts are available as snapshots on Maven Central. In order to get them, add the following lines to your ```settings.xml```/```pom.xml```:

    <repositories>
        ...
        <repository>
            <id>sonatype.oss.snapshots</id>
            <name>Sonatype OSS Snapshot Repository</name>
            <url>http://oss.sonatype.org/content/repositories/snapshots</url>
            <releases>
              <enabled>false</enabled>
            </releases>
            <snapshots>
              <enabled>true</enabled>
            </snapshots>
        </repository>
        ...
    </repositories>

    <dependency>
      <groupId>com.andrearichiardi.eclipse</groupId>
      <artifactId>com.andrearichiardi.eclipse.addons</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
License
-------

[Eclipse Public License 1.0](http://www.eclipse.org/legal/epl-v10.html)

Credits
-------
[The e(fx)clipse team](http://projects.eclipse.org/projects/technology.efxclipse/who) made this possible.
