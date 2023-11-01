# Description

This repository is code submissions for advent of code 2023.

## Usage

### Scaffolding

To create a new day, run the following command:

```bash
./gradlew scaffold --args 1
```

This will generate a new day in the `src/main/kotlin` directory,
along with a test file in the `src/test/kotlin` directory, and
add input to `src/test/resources`.

```bash
src/main/kotlin/Day1.kts
src/test/kotlin/Day1Test.kts
src/test/resources/day_1.txt
```

This requires you to grab your session cookie from adventofcode.com after logging
in and putting it in `src/main/resources/session.txt` as the first line

```txt
fisjdfosidfjsdoifjdsfoijdsfosdijf // <- probably base 64 encoded session ID
```
