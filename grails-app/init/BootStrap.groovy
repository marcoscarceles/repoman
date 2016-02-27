import com.github.marcoscarceles.repoman.AsyncOrganizationService

class BootStrap {

    AsyncOrganizationService asyncOrganizationService

    def init = { servletContext ->
        asyncOrganizationService.saveAllOrganizations()
    }
    def destroy = {
    }
}
