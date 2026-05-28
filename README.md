# Custom Java Maven Archetype

Tento projekt je sablona Maven archetypu. Po instalaci umi generovat zakladni Java projekt s JUnit testem.

## Build archetypu

```bash
mvn clean install
```

Tento prikaz nainstaluje archetyp do lokalniho Maven repozitare a zaroven aktualizuje lokalni katalog:

```text
~/.m2/repository/archetype-catalog.xml
```

## Vygenerovani projektu z lokalniho katalogu

```bash
mvn archetype:generate \
  -DarchetypeCatalog=local \
  -DarchetypeGroupId=cz.kozusznik \
  -DarchetypeArtifactId=custom-java-archetype \
  -DarchetypeVersion=0.1.0 \
  -DgroupId=cz.example.app \
  -DartifactId=demo-app \
  -Dversion=1.0.0-SNAPSHOT \
  -Dpackage=cz.example.app \
  -DjavaVersion=21 \
  -DinteractiveMode=false
```

## Sdileny nebo centralni katalog

Maven nema jeden zapisovatelny "centralni katalog", do ktereho by se archetyp jen pridal prikazem. Obvykly postup je:

1. Publikovat archetype artifact do Maven repozitare, napr. firemni Nexus/Artifactory nebo Maven Central.
2. Vygenerovat `archetype-catalog.xml`.
3. Zverejnit katalog v repozitari nebo na HTTP URL.
4. Pri generovani pouzit `-DarchetypeCatalog=<url-na-archetype-catalog.xml>` nebo `-DarchetypeCatalog=remote`.

Katalog lze vygenerovat nad repozitarem prikazem:

```bash
mvn archetype:crawl
```

Pro firemni repozitar je casty cil mit soubor dostupny napr. zde:

```text
https://repo.example.com/repository/maven-public/archetype-catalog.xml
```

Pak se archetyp pouzije takto:

```bash
mvn archetype:generate \
  -DarchetypeCatalog=https://repo.example.com/repository/maven-public/archetype-catalog.xml
```

## Publikace do Maven Central

Aktualni cesta pro verejne publikovani je Sonatype Central Portal:

```text
https://central.sonatype.com
```

Co musis mit pred publikaci:

1. Ucet na Central Portalu.
2. Overeny namespace `cz.kozusznik`.
3. Release verzi bez `-SNAPSHOT`.
4. GPG klic pro podpis artefaktu.
5. User token z Central Portalu ulozeny v `~/.m2/settings.xml`.

Priklad `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>TVUJ_CENTRAL_TOKEN_USERNAME</username>
      <password>TVUJ_CENTRAL_TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>
```

Pred prvnim releasem uprav v `pom.xml` tyto hodnoty podle skutecnosti:

- `groupId`: musi odpovidat overenemu namespace na Central Portalu.
- `url`: URL verejneho projektu.
- `scm`: URL Git repozitare.
- `developers`: tvoje ID a jmeno.
- `licenses`: licence, pod kterou archetyp vydavas.

Publikace:

```bash
mvn clean deploy -Pcentral-release
```

Projekt je nastaveny na tvuj aktivni GPG klic:

```text
67247C7E58F4B0F6ED1300D1B997A3C2FC6A2541
```

Jiny klic lze pouzit pres:

```bash
mvn clean deploy -Pcentral-release -Dgpg.keyname=JINY_FINGERPRINT
```

Plugin nahraje bundle na Central Portal. V aktualni konfiguraci je `autoPublish=false`, takze po validaci jeste potvrdis vydani rucne na:

```text
https://central.sonatype.com/publishing/deployments
```

Po publikovani uz Maven Central nedovoli danou verzi zmenit ani prepsat. Pro opravu musis vydat novou verzi.

## Co upravit

- `pom.xml`: zmen `groupId`, `artifactId`, `version`, `name` a `description` archetypu.
- `src/main/resources/archetype-resources`: sem patri obsah projektu, ktery se ma generovat.
- `src/main/resources/META-INF/maven/archetype-metadata.xml`: zde se definuji promenne a soubory archetypu.
