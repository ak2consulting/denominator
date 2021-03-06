package denominator.route53;
import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;

import denominator.model.ResourceRecordSet;

enum ToRoute53ResourceRecordSet implements Function<ResourceRecordSet<?>, org.jclouds.route53.domain.ResourceRecordSet> {
    INSTANCE;

    @Override
    public org.jclouds.route53.domain.ResourceRecordSet apply(ResourceRecordSet<?> rrset) {
        return org.jclouds.route53.domain.ResourceRecordSet.builder()
                .name(rrset.getName())
                .type(rrset.getType())
                .ttl(rrset.getTTL().or(300))
                .addAll(toTextFormat(rrset)).build();
    }

    static List<String> toTextFormat(ResourceRecordSet<?> rrset) {
        Builder<String> values = ImmutableList.builder();
        for (Map<String, Object> rdata : rrset) {
            String textFormat = Joiner.on(' ').join(rdata.values());
            if (ImmutableSet.of("SPF", "TXT").contains(rrset.getType())) {
                textFormat = format("\"%s\"", textFormat);
            }
            values.add(textFormat);
        }
        return values.build();
    }

    @Override
    public String toString() {
        return "toRoute53ResourceRecordSet()";
    }
}