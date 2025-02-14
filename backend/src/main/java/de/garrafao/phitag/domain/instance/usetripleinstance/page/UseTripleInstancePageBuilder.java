package de.garrafao.phitag.domain.instance.usetripleinstance.page;

import de.garrafao.phitag.domain.core.PageRequestWraper;

public class UseTripleInstancePageBuilder {

    private int pagesize;
    private int pagenumber;
    private String orderBy;

    public UseTripleInstancePageBuilder() {
    }

    public UseTripleInstancePageBuilder withPageSize(int pagesize) {
        this.pagesize = pagesize;
        return this;
    }

    public UseTripleInstancePageBuilder withPageNumber(int pagenumber) {
        this.pagenumber = pagenumber;
        return this;
    }

    public UseTripleInstancePageBuilder withOrderBy(final String orderBy) {
        this.orderBy = orderBy;
        if (orderBy == null || orderBy.isEmpty()) {
            this.orderBy = "";
        }
        return this;
    }

    public PageRequestWraper build() {
        return new PageRequestWraper(pagesize, pagenumber, orderBy);
    }
    
    
}
