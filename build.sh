#!/usr/bin/env bash

readonly BOLD=$(tput bold)
readonly NORMAL=$(tput sgr0)

main() {
  buildGradle
}

buildGradle() {
  label "build gradle"
  cd application || error "could not cd into application directory"
  ./gradlew clean build check --warning-mode all --configure-on-demand --daemon --parallel
  cd ../
}

label() {
  # \e[K = move cursor to line start
  printf "\e[K%s# %s%s\n" "${BOLD}" "${1}" "${NORMAL}"
  printf " %s...\r" "${*}"
}

info() {
  printf "\e[K> %s\n" "${*}"
}

error () {
  printf "\e[K\033[0;31m%s\n\033[0m" "$*" >&2;
}

main "${@}"