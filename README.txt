Errai Quickstart Archetypes
=======================================

Build using:

    mvn clean install

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
