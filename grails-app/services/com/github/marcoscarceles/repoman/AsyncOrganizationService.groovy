package com.github.marcoscarceles.repoman

import grails.async.DelegateAsync
import grails.transaction.Transactional

@Transactional
class AsyncOrganizationService {
    @DelegateAsync OrganizationService organizationService
}
