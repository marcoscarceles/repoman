package com.github.marcoscarceles.repoman

import grails.async.DelegateAsync

class AsyncOrganizationService {
    @DelegateAsync OrganizationService organizationService
}
