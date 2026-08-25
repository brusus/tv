# builds

Branch generato automaticamente dal workflow `Build`.

Contiene gli artefatti compilati dei plugin (`*.cs3`) e il manifest
`plugins.json` consumato da CloudStream.

Non modificare a mano: a ogni build il workflow riscrive il commit
(`git commit --amend` + `git push --force`), quindi questo branch non
conserva storico.

URL della repo per CloudStream:
https://raw.githubusercontent.com/brusus/tv/builds/plugins.json
