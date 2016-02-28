package com.github.marcoscarceles.repoman.pages

import com.github.marcoscarceles.repoman.modules.OrganizationEntry
import geb.Page

/**
 * Created by @marcos-carceles on 27/02/2016.
 */
class OrganizationListPage extends Page {

    static url = 'organization/index'

    static at = {
        title == "Organization List | RepoMan"
    }

    static content = {
        organizations { moduleList OrganizationEntry, $('#list-organization tbody tr') }
    }

    OrganizationEntry choose(String name) {
        organizations.find { it.name == name }
        //This can be extended to go
    }

    OrganizationEntry choose(int index) {
        organizations[index]
        //This can be extended to go
    }
}
