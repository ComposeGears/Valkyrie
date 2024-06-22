<div align="center">
    <img alt="Icon" src="plugin/src/main/resources/META-INF/pluginIcon.svg" width="200" />
</div>

<h1 align="center">Valkyrie</h1>
<h2 align="center">Intellij IDEA / Android Studio plugin to generate Compose ImageVector from SVG/XML</h2>

## Key features

- built using [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
  and [Tiamat](https://github.com/ComposeGears/Tiamat) navigation library
- support SVG/XML
- convenient code formatting for generated ImageVector icon
- skip default ImageVector path parameters to reduce visual noise
- support drag and drop inside IDE

## Requirements

- Intellij IDEA 2024.1+
- Android Studio Koala+

## Installation

- Manually:

  Download the [latest release](https://github.com/ComposeGears/Valkyrie/releases/latest)
  or [build your self](#Building) and install it manually using
  <kbd>Settings</kbd> -> <kbd>Plugins</kbd> -> <kbd>⚙️</kbd> -> <kbd>Install plugin from disk...</kbd>

## Building

Use `./gradlew buildPlugin` to build plugin locally. Artifact will be available in `plugin/build/distributions/` path

other available gradle commands:

- run plugin in IDE: `./gradlew runIde`

- check dependencies: `./gradlew dependencyUpdates`

- run tests: `./gradlew test`

## License

```
Developed by ComposeGears 2024

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
