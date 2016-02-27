import com.github.marcoscarceles.repoman.AsyncOrganizationService
import com.github.marcoscarceles.repoman.OrganizationService

class BootStrap {

    OrganizationService organizationService
    AsyncOrganizationService asyncOrganizationService

    def init = { servletContext ->
        organizationService.get('Netflix')
        asyncOrganizationService.saveAllOrganizations()
    }
    def destroy = {
    }
}
