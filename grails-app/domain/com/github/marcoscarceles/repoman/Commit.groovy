package com.github.marcoscarceles.repoman

import java.time.ZonedDateTime

/**
 * Created by @marcos-carceles on 28/02/2016.
 */
class Commit {

    String sha
    String message
    String url
    ZonedDateTime date

    String getName() {
        sha
    }
}
