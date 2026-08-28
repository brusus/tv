#!/usr/bin/env bash
# Aggiornamento automatico dei domini dei provider CloudStream (GitHub Actions).
#
# Per ogni modulo attivo cerca i domini dei SITI DI CONTENUTO nelle stringhe
# "https://..." dei sorgenti (escludendo host di hosting video e infrastruttura),
# segue il redirect e, se un sito si e' spostato (host finale diverso, ignorando
# il solo prefisso www, con risposta 200), sostituisce il vecchio host col nuovo
# in TUTTI i .kt del modulo e alza il numero di 'version' nel suo build.gradle.kts.
#
# Non fallisce mai su un singolo dominio: nel dubbio non tocca. Stampa un
# riepilogo; scrive .heal-summary.txt se qualcosa e' cambiato.

set -uo pipefail
UA='Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36'

# Host da NON toccare: hosting video, risolutori, e infrastruttura.
DENY='maxstream|streamtape|mixdrop|m1xdrop|vixcloud|vixsrc|uprot|stayonline|dropload|loadm|flexy|listeamed|supervideo|uqload|dood|deltabit|github|githubusercontent|themoviedb|image\.tmdb|www\.themoviedb|torrentio\.strem|strem\.io|arte\.tv|akamaized|animemapping|realbestia|newkso|schemas\.android|w3\.org|gstatic|googleapis|youtube|skillicons|gnu\.org|imdb\.com|anilist|shindenapi|jsdelivr|freetv|Free-TV'

# Moduli attivi (non commentati) in settings.gradle.kts
mapfile -t MODULES < <(grep -oE '^\s*"[A-Za-z0-9]+"' settings.gradle.kts | tr -d ' "')

norm() { echo "$1" | sed 's/^www\.//'; }

SUMMARY=""
CHANGED_MODULES=()

for mod in "${MODULES[@]}"; do
  [ -d "$mod/src" ] || continue
  # host candidati dai .kt del modulo
  mapfile -t HOSTS < <(grep -rhoE '"https?://[a-zA-Z0-9.-]+' "$mod/src" 2>/dev/null \
      | sed -E 's#^"https?://##' \
      | grep -viE "$DENY" \
      | sort -u)
  mod_changed=0
  for h in "${HOSTS[@]}"; do
    [ -n "$h" ] || continue
    final=$(curl -s -o /dev/null -w '%{http_code} %{url_effective}' -L --max-time 25 -A "$UA" "https://$h/" 2>/dev/null)
    code=$(echo "$final" | awk '{print $1}')
    url=$(echo "$final" | awk '{print $2}')
    [ "$code" = "200" ] || continue
    newhost=$(echo "$url" | sed -E 's#^https?://##; s#/.*$##')
    [ -n "$newhost" ] || continue
    if [ "$(norm "$h")" != "$(norm "$newhost")" ]; then
      echo "~ $mod: $h -> $newhost"
      # sostituisci l'host esatto in tutti i .kt del modulo
      grep -rlE "https?://$h" "$mod/src" 2>/dev/null | while read -r f; do
        sed -i "s#https://$h#https://$newhost#g; s#http://$h#http://$newhost#g" "$f"
      done
      SUMMARY="${SUMMARY}${mod}: ${h} -> ${newhost}\n"
      mod_changed=1
    fi
  done
  if [ "$mod_changed" = "1" ]; then
    CHANGED_MODULES+=("$mod")
  fi
done

# Bump di versione per i moduli modificati
for mod in "${CHANGED_MODULES[@]}"; do
  gradle="$mod/build.gradle.kts"
  [ -f "$gradle" ] || continue
  cur=$(grep -oE '^version = [0-9]+' "$gradle" | grep -oE '[0-9]+' | head -1)
  if [ -n "$cur" ]; then
    new=$((cur + 1))
    sed -i "s/^version = ${cur}\$/version = ${new}/" "$gradle"
    echo "  $mod: version $cur -> $new"
  fi
done

if [ -n "$SUMMARY" ]; then
  printf "%b" "$SUMMARY" > .heal-summary.txt
  echo ""
  echo "Domini aggiornati:"
  printf "%b" "$SUMMARY" | sed 's/^/  /'
else
  echo "Nessun dominio spostato."
fi
