package denominator.profile;

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

import denominator.AllProfileResourceRecordSetApi;
import denominator.model.ResourceRecordSet;
import denominator.model.profile.Geo;

/**
 * list operations are filtered to only include those which are geo record sets.
 */
@Beta
public interface GeoResourceRecordSetApi extends AllProfileResourceRecordSetApi {
    
    /**
     * the set of {@link ResourceRecordSet#getType() record types} that support the geo profile.
     */
    Set<String> getSupportedTypes();

    /**
     * retrieve an organized list of regions by region. It is often the case
     * that the keys correlate to UN or otherwise defined regions such as
     * {@code North America}. However, this can also include special case keys,
     * such as {@code Fallback} and {@code Anonymous Proxy}.
     * <p/>
     * ex.
     * 
     * <pre>
     * {
     *     "States and Provinces: Canada": ["ab", "bc", "mb", "nb", "nl", "nt", "ns", "nu", "on", "pe", "qc", "sk", "yt"],
     *     "Fallback": ["@@"],
     *     "Anonymous Proxy": ["A1"],
     *     "Other Country": ["O1"],
     *     "Satellite Provider": ["A2"]
     * }
     * </pre>
     * 
     * <h4>Note</h4>
     * 
     * The values of this are not guaranteed portable across providers.
     */
    Multimap<String, String> getSupportedRegions();

    /**
     * retrieve a resource record set by name, type, and geo group
     * 
     * @param name
     *            {@link ResourceRecordSet#getName() name} of the rrset
     * @param type
     *            {@link ResourceRecordSet#getType() type} of the rrset
     * @param group
     *            {@link Geo#getGroup() group} of the rrset
     * 
     * @return present if a resource record exists with the same {@code name},
     *         {@code type}, and {@code group}
     * @throws IllegalArgumentException
     *             if the {@code zoneName} is not found.
     */
    Optional<ResourceRecordSet<?>> getByNameTypeAndGroup(String name, String type, String group);

    /**
     * Ensures the supplied {@code regions} are uniform for all record sets with
     * the supplied {@link ResourceRecordSet#getName() name},
     * {@link ResourceRecordSet#getType() type}, and {@link Geo#getName() group}
     * . Returns without error if there are no record sets of the specified
     * name, type, and group.
     * 
     * @param regions
     *            corresponds to {@link Geo#getRegions() regions} you want this
     *            {@code group} to represent. Should be a sub-map of
     *            {@link #getSupportedRegions()}.
     * @param name
     *            {@link ResourceRecordSet#getName() name} of the rrset
     * @param type
     *            {@link ResourceRecordSet#getType() type} of the rrset
     * @param group
     *            {@link Geo#getGroup() group} of the rrset
     */
    void applyRegionsToNameTypeAndGroup(Multimap<String, String> regions, String name, String type, String group);

    /**
     * Ensures the supplied {@code ttl} is uniform for all record sets with the
     * supplied {@link ResourceRecordSet#getName() name},
     * {@link ResourceRecordSet#getType() type}, and {@link Geo#getName() group}
     * . Returns without error if there are no record sets of the specified
     * name, type, and group.
     * 
     * @param ttl
     *            ttl to apply to all records in seconds
     * @param name
     *            {@link ResourceRecordSet#getName() name} of the rrset
     * @param type
     *            {@link ResourceRecordSet#getType() type} of the rrset
     * @param group
     *            {@link Geo#getGroup() group} of the rrset
     */
    void applyTTLToNameTypeAndGroup(int ttl, String name, String type, String group);

    static interface Factory {
        Optional<GeoResourceRecordSetApi> create(String zoneName);
    }
}
