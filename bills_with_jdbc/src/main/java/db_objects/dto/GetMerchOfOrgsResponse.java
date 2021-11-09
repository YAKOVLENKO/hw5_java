package db_objects.dto;

import db_objects.entity.Merch;
import db_objects.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class GetMerchOfOrgsResponse implements Comparable<GetMerchOfOrgsResponse>  {
    @NotNull private Organization organization;
    private Merch merch;

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
