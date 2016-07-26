package com.bima.dokterpribadimu.model;

import java.util.List;

/**
 * Created by apradanas.
 */
public class Onboarding {

    private String templateType;
    private String title;
    private String subtitle;
    private String nextBtnText;
    private String prevBtnText;
    private String backgroundImg;
    private List<OnboardingList> listTemplate;
    private String shareBtnText;
    private String shareText;
    private String shareImg;

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getNextBtnText() {
        return nextBtnText;
    }

    public void setNextBtnText(String nextBtnText) {
        this.nextBtnText = nextBtnText;
    }

    public String getPrevBtnText() {
        return prevBtnText;
    }

    public void setPrevBtnText(String prevBtnText) {
        this.prevBtnText = prevBtnText;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public List<OnboardingList> getListTemplate() {
        return listTemplate;
    }

    public void setListTemplate(List<OnboardingList> listTemplate) {
        this.listTemplate = listTemplate;
    }

    public String getShareBtnText() {
        return shareBtnText;
    }

    public void setShareBtnText(String shareBtnText) {
        this.shareBtnText = shareBtnText;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }
}
