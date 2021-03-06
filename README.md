# All

*Super experimental! no guarantees to the public API surface.*

At the moment, this project is the outcome of the limitations of my search skills.

## Building an artifact

```
./gradlew jar
```

## API

### Usage

```kotlin
val coffeeList = All.of(::Coffee)
  .first(listOf("Attikan"))
  .second(Roast.values().toList())
  .last(GrindSize.values().toList())

coffeeList.onEach(::println)
```

### Output

```kotlin
Coffee(estate=Attikan, roast=Light, grindSize=Fine)
Coffee(estate=Attikan, roast=Light, grindSize=MediumFine)
Coffee(estate=Attikan, roast=Light, grindSize=Medium)
Coffee(estate=Attikan, roast=Light, grindSize=Coarse)
Coffee(estate=Attikan, roast=Medium, grindSize=Fine)
Coffee(estate=Attikan, roast=Medium, grindSize=MediumFine)
Coffee(estate=Attikan, roast=Medium, grindSize=Medium)
Coffee(estate=Attikan, roast=Medium, grindSize=Coarse)
Coffee(estate=Attikan, roast=Dark, grindSize=Fine)
Coffee(estate=Attikan, roast=Dark, grindSize=MediumFine)
Coffee(estate=Attikan, roast=Dark, grindSize=Medium)
Coffee(estate=Attikan, roast=Dark, grindSize=Coarse)
```

## License

```
Copyright 2022—Present Ragunath Jawahar

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
