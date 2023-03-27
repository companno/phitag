class BACKENDROUTES {
    private static BASE_URL = `http://localhost:8080`;
    // private static BASE_URL = `https://phitag.ims.uni-stuttgart.de`;
    private static API_VERSION = `/api/v1`;

    public static AUTHENTICATION = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/authentication`;
    
    public static USER = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/user`;
    public static PROJECT = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/project`;
    public static ANNOTATOR = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/annotator`;
    public static PHASE = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/phase`;
    
    public static PHITAGDATA = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/phitagdata`;
    public static INSTANCE = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/instance`;
    public static JUDGEMENT = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/judgement`;
    public static TASK = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/task`;

    public static CORPUS = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/corpus`;

    public static JOBLISTING = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/joblisting`;

    public static REPORT = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/report`;
    public static NOTIFICATION = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/notification`;

    public static STATISTIC = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/statistic`;
    public static USERSTATISTIC = `${BACKENDROUTES.STATISTIC}/user`;
    public static PROJECTSTATISTIC = `${BACKENDROUTES.STATISTIC}/project`;
    public static ANNOTATORSTATISTIC = `${BACKENDROUTES.STATISTIC}/annotator`;
    public static PHASESTATISTIC = `${BACKENDROUTES.STATISTIC}/phase`;

    // API only containing static data for correct ID mapping 
    public static LANGUAGE = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/language`;
    public static ANNOTATIONTYPE = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/annotationtype`;
    public static VISIBILITY = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/visibility`;
    public static ENTITLEMENT = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/entitlement`;
    public static STATUS = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/status`;
    public static GUIDELINE = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/guideline`;
    public static SAMPLING = `${BACKENDROUTES.BASE_URL}${BACKENDROUTES.API_VERSION}/sampling`;
}

export default BACKENDROUTES;