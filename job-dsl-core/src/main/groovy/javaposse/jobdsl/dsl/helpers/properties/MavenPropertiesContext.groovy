package javaposse.jobdsl.dsl.helpers.properties

import javaposse.jobdsl.dsl.ContextHelper
import javaposse.jobdsl.dsl.DslContext
import javaposse.jobdsl.dsl.Item
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.RequiresPlugin

class MavenPropertiesContext extends PropertiesContext {

    MavenPropertiesContext(JobManagement jobManagement, Item item) {
        super(jobManagement, item)
    }

    /**
     * This plugin add features related to Maven jobs info. Extract information from Maven jobs (update name and
     * description from POM)
     *
     * @since 1.46
     */
    @RequiresPlugin(id = 'maven-info', minimumVersion = '0.2.0')
    void mavenInfo(@DslContext(MavenInfoContext) Closure closure) {
        MavenInfoContext context = new MavenInfoContext()
        ContextHelper.executeInContext(closure, context)

        propertiesNodes << new NodeBuilder().'jenkins.plugins.maveninfo.config.MavenInfoJobConfig' {
            assignName(context.assignName)
            nameTemplate(context.name ?: '')
            assignDescription(context.assignDescription)
            if (context.assignDescription) {
                descriptionTemplate(context.description ?: '')
            }
            mainModulePattern(context.modulePattern ?: '')
            dependenciesPattern(context.interestingDependenciesPattern ?: '')
        }
    }

}
