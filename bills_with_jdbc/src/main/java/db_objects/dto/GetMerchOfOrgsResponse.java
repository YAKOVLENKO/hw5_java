package db_objects.dto;

import db_objects.dao.DAOItems;
import db_objects.entity.Merch;
import db_objects.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
public class GetMerchOfOrgsResponse implements Comparable<GetMerchOfOrgsResponse>  {
    @NotNull private Organization organization;
    private Merch merch;

    public GetMerchOfOrgsResponse(@NotNull ResultSet resultSet, @NotNull DAOItems dao) throws SQLException {
        int orgId = resultSet.getInt("id");
        int merchId = resultSet.getInt("merch_id");
        if (merchId > 0) {
            this.setMerch(dao.getMerchDAO().select(merchId));
        }
        this.setOrganization(dao.getOrganizationDAO().select(orgId));
    }

    @Override
    public int compareTo(@NotNull GetMerchOfOrgsResponse o) {
        int subOrgs = this.organization.getId() - o.organization.getId();
        if (subOrgs == 0) {
            if (this.merch == null) {
                if (o.merch == null)
                    return 0;
                return -1;
            }
            if (o.merch == null) return 1;
            return this.merch.getId() - o.merch.getId();
        }
        return subOrgs;
    }
}
