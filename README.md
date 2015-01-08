eclipse-addons
==============

I found e(fx)clipse ```core.di```, ```core.adapter```, ```core.text``` packages very good additions to Eclipse's own framework and then decided to factor out their code in an autonomous bundle: ```com.andrearichiardi.eclipse.addons```.

To understand its features, try either [this](http://tomsondev.bestsolution.at/2013/11/21/writing-ieclipsecontext-less-code/) blog post or [this](https://wiki.eclipse.org/Efxclipse/Runtime/Recipes#Publishing_to_the_IEclipseContext) other excerpt from the e(fx)clipse wiki

The new packaging removes all the Java 8 type annotations and adds [retrolambda](https://github.com/orfjackal/retrolambda) to backport new constructs to Java 6. Unfortunately, the ```core.text``` package makes use of ```java.util.function``` and ```java.time```, and it has been momentarily excluded. Pull requests that finalize a JDK8-only build through a Maven profile are welcome. Kepler and E4 (Luna and higher) are supported.

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

Contribute
----------
    
In order to properly build using Maven, [```toolchains.xml```](http://maven.apache.org/guides/mini/guide-using-toolchains.html) needs to be configured with a suitable 1.6 path as the Execution Environment has been set to ```JavaSE-1.6``` and Tycho's [```<useJDK>```](http://eclipse.org/tycho/sitedocs/tycho-compiler-plugin/compile-mojo.html#useJDK) to ```BREE```.

License
-------

[Eclipse Public License 1.0](http://www.eclipse.org/legal/epl-v10.html)

Credits
-------
[The e(fx)clipse team](http://projects.eclipse.org/projects/technology.efxclipse/who) made this possible.
