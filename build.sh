#!/usr/bin/env bash

##
# central build file for local builds, git hooks and github actions
##

[[ -t 1 ]] || export TERM=dumb
readonly BOLD=$(tput bold)
readonly RED=$(tput setaf 1)
readonly NORMAL=$(tput sgr0)

readonly EXIT_CODE_ERROR=2

main() {
  buildGradle
}

buildGradle() {
  label "build gradle"
  local status
  cd application || error "could not cd into application directory"
  ./gradlew clean build check --warning-mode fail --daemon --parallel
  status=$?
  cd ../

  if (( status != 0 )); then
    exit "${status}"
  fi
}

label() {
  printf "%s# %s%s\n" "${BOLD}" "${1}" "${NORMAL}"
  printf " %s...\r" "${*}"
}

info() {
  printf "> %s\n" "${*}"
}

error () {
  printf "%s%s\n%s" "${RED}" "$*" "${NORMAL}" >&2;
  exit "${EXIT_CODE_ERROR}"
}

main "${@}"
