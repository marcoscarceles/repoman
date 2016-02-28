# repoman
Github's REPOsitory MANager

[![Build Status](https://snap-ci.com/marcoscarceles/repoman/branch/master/build_image)](https://snap-ci.com/marcoscarceles/repoman/branch/master)

To run this application you'll need:
 * Grails 3.1.1
 * Java 8
 
Use one of the following environment variables combinations to integrate with Github:
 * ClientID / Secret based:
   * `GTIHUB_SECRET`
   * `GITHUB_CLIENTID`
 * Token based (Useful for development)
   * `GITHUB_TOKEN` : A valid Github OAuth Token (available for development via https://github.com/settings/tokens/new)
 * User based (Useful for development)
   * `GITHUB_USERNAME`
   * `GITHUB_PASSWORD`
