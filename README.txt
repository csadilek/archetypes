Errai Quickstart Archetypes
=======================================

Build using:

    mvn clean install


Everything But The Kitchen Sink
---------------------------------------

This explanation applies to all archetypes *except* jboss-errai-kitchensink-archetype:

By default, the project version of the archetypes themselves will be used as the version of Errai.
So if this archetype parent pom is version 2.0.Final, then the archetypes will generate projects
that depend on Errai 2.0.Final.

Sometimes, you will want to release archetypes whose versions don't match the version of Errai
they depend on. For example, if there was an error in the 2.0.Final archetypes themselves, you
might want to relase archetypes with version 2.0.1.Final that still depend on Errai 2.0.Final.
You would achieve that with the property:

    -Derrai.target.version=x.y.z

To illustrate the case above, you would update pom.xml in this directory to version 2.0.1.Final,
then build like this:

    mvn clean install -Derrai.target.version=2.0.Final


Kitchen Sink: it's da BOM!
---------------------------------------

The jboss-errai-kitchensink-archetype feeds into the JBoss Tools/JBDS welcome page. It is
required to inherit its version information from the JBoss BOM (Bill Of Materials) system.
This means we have no direct control over the version of Errai that this archetype depends
on. To adjust the Errai target version for the Kitchen Sink archetype, you have to:

1. edit the root BOM at https://github.com/jboss-jdf/jboss-bom/blob/master/pom.xml
   (look for the version.org.jboss.errai property)
2. submit a pull request back to the jboss-boms project
3. wait for the pull request to be merged
4. wait for jboss-boms to be published with a non-snapshot version number
5. update the BOM version in two places in jboss-errai-kitchensink-archetype/src/main/resources/archetype-resources/pom.xml
6. Wait until the newly published BOM propagates to Maven Central before releasing the Errai
   archetype

I know.


Skipping the Tests
=======================================

The tests are very valuable, but also time-consuming because they depoloy to app servers. If
you're just pulling in someone's already-tested changes. To skip the tests, just build like this:

    mvn clean install -Darchetype.test.skip=true


Troubleshooting
=======================================

If you run into trouble don't hesitate to get in touch with us:

User Forum:     http://community.jboss.org/en/errai?view=discussions 
Mailing List:   http://jboss.org/errai/MailingLists.html
IRC:            irc://irc.freenode.net/errai

Have fun,
The Errai Team
