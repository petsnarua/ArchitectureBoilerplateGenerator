Overview
=

Recently I started off working for Unicred Mobile Banking as a Senior Android Engineer.

The app had a huge monolithic and legacy codebase written in Java. So, my first task at Unicred was to define a strategy of modularization and refactoring on the app.

We had to separate the app, initially,  into three layers which are: the ```domain```, ```data```, and ```presentation```.

Since the code was very tightly coupled, I decided, at first, to place all application entities within the ```domain layer```.

However, we know how many boilerplate we need to write to create a new ```feature-module```, don't we? ```ViewModel```, ```Activity or Fragment```, ```UseCases```, ```Many interfaces```, ```Datasources and Repositories``` and so on. So in order to turn this process easier, I built this "architecture-code-generator" to create all of the templates we need on the project for each new feature.

## How to use this generator

TODO

## Creating an empty feature

To start your generator you will need a [ProjectSettings](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/library/src/main/java/com/mctech/architecture/generator/settings/ProjectSettings.kt) instance and a [FeatureSettings](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/library/src/main/java/com/mctech/architecture/generator/settings/FeatureSettings.kt) instance. Here is an example:

```kotlin
fun main() {
    val projectSettings = ProjectSettings(
        basePackageName = Package("com.mctech.architecture")
    )

    val featureSettings = FeatureSettings(
        createDependencyInjectionModules = false,
        projectSettings         = projectSettings,
        fileDuplicatedStrategy  = FileDuplicatedStrategy.Replace,
        presentationViewModel   = PresentationMode.ActivityAndFragment,
    )

    // Here is an empty feature generated
    FeatureGenerator(
        settings    = featureSettings,
        featureName = "FeatureEmpty"
    ).newFeature {}
}
```

All you are going to do is run this ```main function``` and the files are going to be generated.

## Generated files

Domain Layer
* [FeatureEmpty.kt](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/domain/src/main/java/com/mctech/architecture/domain/feature_empty/entity/FeatureEmpty.kt)
* [FeatureEmptyService.kt](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/domain/src/main/java/com/mctech/architecture/domain/feature_empty/service/FeatureEmptyService.kt)

Data Layer
* [FeatureEmptyDataSource.kt](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/data/src/main/java/com/mctech/architecture/data/feature_empty/datasource/FeatureEmptyDataSource.kt)
* [LocalFeatureEmptyDataSource.kt](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/data/src/main/java/com/mctech/architecture/data/feature_empty/datasource/LocalFeatureEmptyDataSource.kt)
* [FeatureEmptyRepository.kt](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/data/src/main/java/com/mctech/architecture/data/feature_empty/repository/FeatureEmptyRepository.kt)
* [FeatureEmptyAPI.kt](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/data/src/main/java/com/mctech/architecture/data/feature_empty/api/FeatureEmptyAPI.kt)

Presentation Layer (New Module)
* [settings.gradle](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/settings.gradle) - Add new module on the current project file.
* [build.gradle](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/features/feature-feature-empty/build.gradle)
* [AndroidManifest.xml](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/features/feature-feature-empty/src/main/AndroidManifest.xml)
* [strings.xml](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/features/feature-feature-empty/src/main/res/values/strings.xml)
* [activity_feature_empty.xml](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/features/feature-feature-empty/src/main/res/layout/activity_feature_empty.xml)
* [fragment_feature_empty.xml](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator/blob/master/sample/features/feature-feature-empty/src/main/res/layout/fragment_feature_empty.xml)
