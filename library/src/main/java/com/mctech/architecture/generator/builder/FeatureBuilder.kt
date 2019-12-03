package com.mctech.architecture.generator.builder

import com.mctech.architecture.generator.alias.*
import com.mctech.architecture.generator.class_contract.Type
import com.mctech.architecture.generator.context.FeatureContext
import com.mctech.architecture.generator.path.ModuleDefaultLayers
import com.mctech.architecture.generator.settings.FeatureSettings
import com.mctech.architecture.generator.settings.GlobalSettings
import com.mctech.architecture.generator.templates.data.api.RetrofitAPITemplate
import com.mctech.architecture.generator.templates.data.datasource.DataSourceInterfaceTemplate
import com.mctech.architecture.generator.templates.data.datasource.LocalDataSourceTemplate
import com.mctech.architecture.generator.templates.data.datasource.RemoteDataSourceTemplate
import com.mctech.architecture.generator.templates.data.repository.RepositoryTemplate
import com.mctech.architecture.generator.templates.domain.entity.EmptyEntityTemplate
import com.mctech.architecture.generator.templates.domain.interaction.UseCaseTemplate
import com.mctech.architecture.generator.templates.domain.service.ServiceInterfaceTemplate

/**
 * @author MAYCON CARDOSO on 2019-11-27.
 */
class FeatureGenerator(val settings: FeatureSettings, featureName: FeatureName) {
    init {
        // Set global settings with the current feature.
        GlobalSettings.projectSettings = settings.projectSettings
        GlobalSettings.currentFeatureName = featureName
        GlobalSettings.fileDuplicatedStrategy = settings.fileDuplicatedStrategy

        if (settings.createDependencyInjectionModules) {
            throw RuntimeException("The library does not support Dependency Injection generation yet.")
        }
    }

    // Architecture layers
    var dataModulePath      = ModuleDefaultLayers.Data.moduleFile
    var domainModulePath    = ModuleDefaultLayers.Domain.moduleFile
    var featureModulePath   = ModuleDefaultLayers.GeneratedFeature.moduleFile

    // Templates Generators
    var entityTemplateGenerator             : FeatureEntityTemplate             = EmptyEntityTemplate(domainModulePath)
    var serviceGenerator                    : FeatureServiceTemplate            = ServiceInterfaceTemplate(domainModulePath)
    var serviceGeneratorImplTemplate        : FeatureServiceImplTemplate        = RepositoryTemplate(dataModulePath)
    var dataSourceTemplateGenerator         : FeatureDataSourceTemplate         = DataSourceInterfaceTemplate(dataModulePath)
    var localDataSourceTemplateGenerator    : FeatureLocalDataSourceTemplate    = LocalDataSourceTemplate(dataModulePath)
    var remoteDataSourceTemplateGenerator   : FeatureRemoteDataSourceTemplate   = RemoteDataSourceTemplate(dataModulePath)
    var retrofitAPITemplateGenerator        : FeatureRetrofitAPITemplate        = RetrofitAPITemplate(dataModulePath)

    // Use cases
    val listOfUseCases = mutableListOf<UseCaseBuilder>()

    // Live data
    val listOfLiveData = mutableListOf<String>()

    /**
     * Every UseCase added on the feature will create the following code:
     * - Use case file
     * - Method signature on services, repositories, data sources and API files.
     */
    fun addUseCase(block : () -> UseCaseBuilder) {
        listOfUseCases.add(block.invoke().apply {
            modulePath = domainModulePath
        })
    }

    fun addLiveData(
        name: String,
        dataType: Type = Type.Unit
    ) {

    }

    fun addComponentState(
        name: String,
        dataType: Type = Type.Unit
    ) {

    }

    /**
     * Called in order to perform the code generation and create all of the files.
     */
    fun generate() {
        // Set context
        FeatureContext.featureGenerator = this

        // Generate files
        entityTemplateGenerator.generate()
        serviceGenerator.generate()
        serviceGeneratorImplTemplate.generate()
        dataSourceTemplateGenerator.generate()
        localDataSourceTemplateGenerator.generate()

        // Only if the feature has remote dataSource.
        if(settings.createBothRemoteAndLocalDataSources){
            remoteDataSourceTemplateGenerator.generate()
            retrofitAPITemplateGenerator.generate()
        }

        // Create all UseCases
        listOfUseCases.forEach {
            UseCaseTemplate(it, domainModulePath).generate()
        }
    }
}

/**
 * Function to facilitate when creating features.
 */
inline fun FeatureGenerator.newFeature(block: FeatureGenerator.() -> Unit): FeatureGenerator {
    block()
    generate()
    return this
}