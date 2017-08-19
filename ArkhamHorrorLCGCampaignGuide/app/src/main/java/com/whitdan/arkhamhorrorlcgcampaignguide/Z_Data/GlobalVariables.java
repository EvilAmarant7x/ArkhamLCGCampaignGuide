package com.whitdan.arkhamhorrorlcgcampaignguide.Z_Data;

import android.app.Application;

import java.util.ArrayList;

/*
    GlobalVariables - Contains all the variables which need to persist between activities
 */

public class GlobalVariables extends Application {

    /*
        Campaign variables - All of the variables associated with the campaign generally
     */
    public long CampaignID;                        // Corresponds to the SQLite database
    public int CurrentCampaign;                    // 1 = Night, 2 = Dunwich, 3 = Carcosa
    public int CurrentScenario;                    // 0 = campaign setup, >100 = standalone, 1000 = between campaigns
    public int CurrentDifficulty;                  // 0 = easy, 1 = normal, 2 = hard, 3 = expert (except standalones)
    public int NightCompleted;
    public int DunwichCompleted;

    /*
        Scenario variables - All of the variables associated with the scenario generally
     */
    public int ScenarioResolution;                 // 0 = no resolution
    public int VictoryDisplay;

    /*
        Investigator variables - All of the variables associated with the investigators generally
     */
    public ArrayList<Investigator> Investigators = new ArrayList<>();
    public ArrayList<Investigator> SavedInvestigators = new ArrayList<>();
    public ArrayList<Integer> InvestigatorNames = new ArrayList<>();
    public String[] PlayerNames = new String[4];
    public String[] DeckNames = new String[4];
    public String[] DeckLists = new String[4];
    public int[] InvestigatorsInUse = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};      // Matches up to the names in the string
                                                                                // array

    /*
        Night of the Zealot variables - All of the variables associated with the Night of the Zealot campaign
     */
    public int HouseStanding;       // 0 = burned, 1 = standing0
    public int GhoulPriest;         // 0 = dead, 1 = alive
    public int LitaChantler;        // 0 = not obtained, 1 = obtained, 2 = forced to find others
    public int PastMidnight;
    public int Umordhoth;
    // All of the cultists
    public int CultistsInterrogated = 0;
    public int DrewInterrogated;
    public int HermanInterrogated;
    public int PeterInterrogated;
    public int VictoriaInterrogated;
    public int RuthInterrogated;
    public int MaskedInterrogated;

    /*
        The Dunwich Legacy variables - All of the variables associated with The Dunwich Legacy campaign
     */
    public int FirstScenario;
    public int InvestigatorsUnconscious;
    public int HenryArmitage;
    public int WarrenRice;
    public int Students;
    public int ObannionGang;
    public int FrancisMorgan;
    public int InvestigatorsCheated;
    public int Necronomicon;
    public int AdamLynchHaroldWalsted;
    public int InvestigatorsDelayed;
    public int EngineCar;
    public int SilasBishop;
    public int ZebulonWhateley;
    public int EarlSawyer;
    public int AllySacrificed;
    public int TownsfolkAction;     // 0 = not set, 1 = calmed, 2 = warned
    public int BroodsEscaped;
    public int InvestigatorsGate;
    public int YogSothoth;
    /*
        Side story variables - All of the variables associated with side stories
     */
    public int PreviousScenario;
    public int Rougarou;
    public int Carnevale;
    public int CarnevaleReward;

    /*
        Player cards - All of the variables associated with specific player cards
     */
    public int StrangeSolution;
    public int ArchaicGlyphs;
}
