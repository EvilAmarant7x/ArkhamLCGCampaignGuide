package com.whitdan.arkhamhorrorlcgcampaignguide.C_Scenario;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whitdan.arkhamhorrorlcgcampaignguide.A_Menus.MainMenuActivity;
import com.whitdan.arkhamhorrorlcgcampaignguide.D_Misc.CampaignFinishedActivity;
import com.whitdan.arkhamhorrorlcgcampaignguide.D_Misc.CampaignLogActivity;
import com.whitdan.arkhamhorrorlcgcampaignguide.R;
import com.whitdan.arkhamhorrorlcgcampaignguide.Z_Data.ArkhamContract;
import com.whitdan.arkhamhorrorlcgcampaignguide.Z_Data.ArkhamDbHelper;
import com.whitdan.arkhamhorrorlcgcampaignguide.Z_Data.GlobalVariables;
import com.whitdan.arkhamhorrorlcgcampaignguide.Z_Data.Investigator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.whitdan.arkhamhorrorlcgcampaignguide.R.array.investigators;
import static com.whitdan.arkhamhorrorlcgcampaignguide.R.string.defeated;
import static com.whitdan.arkhamhorrorlcgcampaignguide.R.string.drew;

public class ScenarioResolutionActivity extends AppCompatActivity {

    static GlobalVariables globalVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // If app is reopening after the process is killed, kick back to the main menu (stops the activity from
        // showing up unpopulated)
        if (savedInstanceState != null) {
            Intent intent = new Intent(ScenarioResolutionActivity.this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_scenario_resolution);
        globalVariables = (GlobalVariables) this.getApplication();
        globalVariables.ScenarioResolution = -1;

        // Set fonts
        Typeface teutonic = Typeface.createFromAsset(getAssets(), "fonts/teutonic.ttf");
        Typeface arnopro = Typeface.createFromAsset(getAssets(), "fonts/arnopro.otf");
        Typeface arnoprobold = Typeface.createFromAsset(getAssets(), "fonts/arnoprobold.otf");
        Typeface arnoproitalic = Typeface.createFromAsset(getAssets(), "fonts/arnoproitalic.otf");
        Typeface wolgast = Typeface.createFromAsset(getAssets(), "fonts/wolgast.otf");
        Typeface wolgastbold = Typeface.createFromAsset(getAssets(), "fonts/wolgastbold.otf");
        TextView title = (TextView) findViewById(R.id.current_scenario_name);
        title.setTypeface(teutonic);
        TextView subTitle = (TextView) findViewById(R.id.scenario_resolution);
        subTitle.setTypeface(teutonic);
        TextView defeatedHeading = (TextView) findViewById(R.id.investigators_defeated_heading);
        defeatedHeading.setTypeface(teutonic);
        TextView selectHeading = (TextView) findViewById(R.id.select_resolution_heading);
        selectHeading.setTypeface(teutonic);
        LinearLayout additionalCounterLayout = (LinearLayout) findViewById(R.id.additional_counter_layout);
        final TextView additionalCounter = (TextView) findViewById(R.id.additional_counter);
        additionalCounter.setTypeface(arnoprobold);
        TextView additionalDecrement = (TextView) findViewById(R.id.additional_decrement);
        additionalDecrement.setTypeface(wolgast);
        final TextView additionalAmount = (TextView) findViewById(R.id.additional_amount);
        additionalAmount.setTypeface(wolgastbold);
        TextView additionalIncrement = (TextView) findViewById(R.id.additional_increment);
        additionalIncrement.setTypeface(wolgast);
        TextView resolution = (TextView) findViewById(R.id.resolution_text);
        resolution.setTypeface(arnoproitalic);
        final TextView resolutionAdditional = (TextView) findViewById(R.id.resolution_text_additional);
        resolutionAdditional.setTypeface(arnoproitalic);

        // Victory display
        TextView victoryDisplay = (TextView) findViewById(R.id.victory_display);
        victoryDisplay.setTypeface(arnoprobold);
        TextView victoryDecrement = (TextView) findViewById(R.id.victory_decrement);
        victoryDecrement.setTypeface(wolgast);
        final TextView victoryAmount = (TextView) findViewById(R.id.victory_amount);
        victoryAmount.setTypeface(wolgastbold);
        TextView victoryIncrement = (TextView) findViewById(R.id.victory_increment);
        victoryIncrement.setTypeface(wolgast);
        victoryAmount.setText(String.valueOf(globalVariables.VictoryDisplay));
        victoryDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = globalVariables.VictoryDisplay;
                if (current > 0) {
                    globalVariables.VictoryDisplay += -1;
                    victoryAmount.setText(String.valueOf(globalVariables.VictoryDisplay));
                }
            }
        });
        victoryIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = globalVariables.VictoryDisplay;
                if (current < 99) {
                    globalVariables.VictoryDisplay += 1;
                    victoryAmount.setText(String.valueOf(globalVariables.VictoryDisplay));
                }
            }
        });

        // Set title
        switch (globalVariables.CurrentCampaign) {
            case 1:
                switch (globalVariables.CurrentScenario) {
                    case 1:
                        title.setText(R.string.night_scenario_one);
                        break;
                    case 2:
                        title.setText(R.string.night_scenario_two);
                        break;
                    case 3:
                        title.setText(R.string.night_scenario_three);
                        break;
                }
                break;
            case 2:
                switch (globalVariables.CurrentScenario) {
                    case 1:
                        title.setText(R.string.dunwich_scenario_one);
                        break;
                    case 2:
                        title.setText(R.string.dunwich_scenario_two);
                        break;
                    case 3:
                        title.setText(R.string.dunwich_interlude_one);
                        break;
                    case 4:
                        title.setText(R.string.dunwich_scenario_three);
                        break;
                    case 5:
                        title.setText(R.string.dunwich_scenario_four);
                        break;
                    case 6:
                        title.setText(R.string.dunwich_scenario_five);
                        break;
                    case 7:
                        title.setText(R.string.dunwich_interlude_two);
                        break;
                    case 8:
                        title.setText(R.string.dunwich_scenario_six);
                        break;
                    case 9:
                        title.setText(R.string.dunwich_scenario_seven);
                        break;
                    case 10:
                        title.setText(R.string.dunwich_scenario_eight);
                        break;
                    case 11:
                        title.setText(R.string.dunwich_epilogue);
                        break;
                }
                break;
            case 3:
                break;
        }
        if (globalVariables.CurrentScenario > 100) {
            switch (globalVariables.CurrentScenario) {
                case 101:
                    title.setText(R.string.rougarou_scenario);
                    break;
                case 102:
                    title.setText(R.string.carnevale_scenario);
                    break;
            }
        }

        // Set fonts to the defeated layout
        TextView investigatorOneName = (TextView) findViewById(R.id.investigator_one_name);
        investigatorOneName.setTypeface(arnoprobold);
        CheckBox resignedOne = (CheckBox) findViewById(R.id.resigned_button_one);
        resignedOne.setTypeface(arnopro);
        TextView investigatorTwoName = (TextView) findViewById(R.id.investigator_two_name);
        investigatorTwoName.setTypeface(arnoprobold);
        CheckBox resignedTwo = (CheckBox) findViewById(R.id.resigned_button_two);
        resignedTwo.setTypeface(arnopro);
        TextView investigatorThreeName = (TextView) findViewById(R.id.investigator_three_name);
        investigatorThreeName.setTypeface(arnoprobold);
        CheckBox resignedThree = (CheckBox) findViewById(R.id.resigned_button_three);
        resignedThree.setTypeface(arnopro);
        TextView investigatorFourName = (TextView) findViewById(R.id.investigator_four_name);
        investigatorFourName.setTypeface(arnoprobold);
        CheckBox resignedFour = (CheckBox) findViewById(R.id.resigned_button_four);
        resignedFour.setTypeface(arnopro);

        // Show the relevant defeated layouts and set the right investigator names
        LinearLayout defeatedOne = (LinearLayout) findViewById(R.id.investigator_one_defeated_layout);
        LinearLayout defeatedTwo = (LinearLayout) findViewById(R.id.investigator_two_defeated_layout);
        LinearLayout defeatedThree = (LinearLayout) findViewById(R.id.investigator_three_defeated_layout);
        LinearLayout defeatedFour = (LinearLayout) findViewById(R.id.investigator_four_defeated_layout);
        LinearLayout defeatedOneSelection = (LinearLayout) findViewById(R.id.defeated_one_selection);
        LinearLayout defeatedTwoSelection = (LinearLayout) findViewById(R.id.defeated_two_selection);
        LinearLayout defeatedThreeSelection = (LinearLayout) findViewById(R.id.defeated_three_selection);
        LinearLayout defeatedFourSelection = (LinearLayout) findViewById(R.id.defeated_four_selection);
        final String[] investigatorNames = getResources().getStringArray(investigators);
        switch (globalVariables.Investigators.size()) {
            case 4:
                defeatedFour.setVisibility(VISIBLE);
                investigatorFourName.setText(investigatorNames[globalVariables.Investigators.get(3).Name]);
            case 3:
                defeatedThree.setVisibility(VISIBLE);
                investigatorThreeName.setText(investigatorNames[globalVariables.Investigators.get(2).Name]);
            case 2:
                defeatedTwo.setVisibility(VISIBLE);
                investigatorTwoName.setText(investigatorNames[globalVariables.Investigators.get(1).Name]);
            case 1:
                defeatedOne.setVisibility(VISIBLE);
                investigatorOneName.setText(investigatorNames[globalVariables.Investigators.get(0).Name]);
        }
        for (int i = 0; i < defeatedOneSelection.getChildCount(); i++) {
            View view = defeatedOneSelection.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setOnCheckedChangeListener(new InvestigatorDefeatedCheckboxListener());
            }
            if (view instanceof ImageView) {
                view.setOnClickListener(new InvestigatorDefeatedImageListener());
            }
        }
        for (int i = 0; i < defeatedTwoSelection.getChildCount(); i++) {
            View view = defeatedTwoSelection.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setOnCheckedChangeListener(new InvestigatorDefeatedCheckboxListener());
            }
            if (view instanceof ImageView) {
                view.setOnClickListener(new InvestigatorDefeatedImageListener());
            }
        }
        for (int i = 0; i < defeatedThreeSelection.getChildCount(); i++) {
            View view = defeatedThreeSelection.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setOnCheckedChangeListener(new InvestigatorDefeatedCheckboxListener());
            }
            if (view instanceof ImageView) {
                view.setOnClickListener(new InvestigatorDefeatedImageListener());
            }
        }
        for (int i = 0; i < defeatedFourSelection.getChildCount(); i++) {
            View view = defeatedFourSelection.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setOnCheckedChangeListener(new InvestigatorDefeatedCheckboxListener());
            }
            if (view instanceof ImageView) {
                view.setOnClickListener(new InvestigatorDefeatedImageListener());
            }
        }

        // Set fonts and listeners for select resolution
        final RadioGroup selectResolution = (RadioGroup) findViewById(R.id.select_resolution);
        for (int i = 0; i < selectResolution.getChildCount(); i++) {
            View view = selectResolution.getChildAt(i);
            if (view instanceof RadioButton) {
                ((RadioButton) view).setTypeface(arnoprobold);
            }
        }
        selectResolution.setOnCheckedChangeListener(new ResolutionCheckboxListener());

        // Show right number of checkboxes (default is two resolutions)
        RadioButton resolutionTwo = (RadioButton) findViewById(R.id.resolution_two);
        RadioButton resolutionThree = (RadioButton) findViewById(R.id.resolution_three);
        RadioButton resolutionFour = (RadioButton) findViewById(R.id.resolution_four);
        switch (globalVariables.CurrentCampaign) {
            case 1:
                switch (globalVariables.CurrentScenario) {
                    case 1:
                    case 3:
                        resolutionThree.setVisibility(VISIBLE);
                        break;
                }
                break;
            case 2:
                switch (globalVariables.CurrentScenario) {
                    case 1:
                    case 2:
                        resolutionThree.setVisibility(VISIBLE);
                        resolutionFour.setVisibility(VISIBLE);
                        break;
                    case 10:
                        resolutionTwo.setVisibility(GONE);
                        resolutionThree.setVisibility(VISIBLE);
                        break;
                }
                break;
            case 3:
                break;
        }
        if (globalVariables.CurrentScenario > 100) {
            switch (globalVariables.CurrentScenario) {
                case 101:
                    resolutionThree.setVisibility(VISIBLE);
                    break;
            }
        }

        // Setup all additional views (mostly additional checkboxes) if required for the scenario
        final CheckBox additionalCheckbox = (CheckBox) findViewById(R.id.additional_checkbox_one);
        final CheckBox additionalCheckboxTwo = (CheckBox) findViewById(R.id.additional_checkbox_two);
        additionalCheckbox.setTypeface(arnopro);
        additionalCheckboxTwo.setTypeface(arnopro);
        TextView additionalGroupHeading = (TextView) findViewById(R.id.additional_group_heading);
        additionalGroupHeading.setTypeface(teutonic);
        final TextView selectInvestigatorHeading = (TextView) findViewById(R.id.select_investigator_heading);
        selectInvestigatorHeading.setTypeface(teutonic);
        final LinearLayout selectInvestigator = (LinearLayout) findViewById(R.id.select_investigator);
        final CheckBox selectInvestigatorOne = (CheckBox) findViewById(R.id.select_investigator_one);
        final CheckBox selectInvestigatorTwo = (CheckBox) findViewById(R.id.select_investigator_two);
        final CheckBox selectInvestigatorThree = (CheckBox) findViewById(R.id.select_investigator_three);
        final CheckBox selectInvestigatorFour = (CheckBox) findViewById(R.id.select_investigator_four);
        selectInvestigatorOne.setTypeface(arnopro);
        selectInvestigatorTwo.setTypeface(arnopro);
        selectInvestigatorThree.setTypeface(arnopro);
        selectInvestigatorFour.setTypeface(arnopro);
        LinearLayout additionalGroup = (LinearLayout) findViewById(R.id.additional_group);
        CheckBox additionalGroupOne = (CheckBox) findViewById(R.id.additional_group_one);
        CheckBox additionalGroupTwo = (CheckBox) findViewById(R.id.additional_group_two);
        CheckBox additionalGroupThree = (CheckBox) findViewById(R.id.additional_group_three);
        CheckBox additionalGroupFour = (CheckBox) findViewById(R.id.additional_group_four);
        CheckBox additionalGroupFive = (CheckBox) findViewById(R.id.additional_group_five);
        CheckBox additionalGroupSix = (CheckBox) findViewById(R.id.additional_group_six);
        CheckBox additionalGroupSeven = (CheckBox) findViewById(R.id.additional_group_seven);
        additionalGroupOne.setTypeface(arnopro);
        additionalGroupTwo.setTypeface(arnopro);
        additionalGroupThree.setTypeface(arnopro);
        additionalGroupFour.setTypeface(arnopro);
        additionalGroupFive.setTypeface(arnopro);
        additionalGroupSix.setTypeface(arnopro);
        additionalGroupSeven.setTypeface(arnopro);
        switch (globalVariables.CurrentCampaign) {
            case 1:
                switch (globalVariables.CurrentScenario) {
                    // The Midnight Masks
                    case 2:
                        // Ghoul Priest checkbox
                        if (globalVariables.GhoulPriest == 1) {
                            additionalCheckbox.setVisibility(VISIBLE);
                            additionalCheckbox.setText(R.string.ghoul_priest_checkbox);
                        }
                        // Setup cultists interrogated view
                        additionalGroupHeading.setVisibility(VISIBLE);
                        additionalGroupHeading.setText(R.string.cultists_interrogated_resolution);
                        additionalGroup.setVisibility(VISIBLE);
                        additionalGroupOne.setText(drew);
                        additionalGroupTwo.setText(R.string.peter);
                        additionalGroupThree.setText(R.string.ruth);
                        additionalGroupFour.setText(R.string.herman);
                        additionalGroupFive.setText(R.string.victoria);
                        additionalGroupSix.setText(R.string.masked_hunter);
                        break;
                    case 3:
                        // Ghoul Priest checkbox
                        if (globalVariables.GhoulPriest == 1) {
                            additionalCheckbox.setVisibility(VISIBLE);
                            additionalCheckbox.setText(R.string.ghoul_priest_checkbox);
                        }
                        break;
                }
                break;
            case 2:
                switch (globalVariables.CurrentScenario) {
                    case 2:
                        // Cheated checkbox
                        additionalCheckbox.setVisibility(VISIBLE);
                        additionalCheckbox.setText(R.string.cheated_checkbox);
                        additionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (compoundButton.isChecked()) {
                                    resolutionAdditional.setVisibility(VISIBLE);
                                    resolutionAdditional.setText(R.string.cheated_text);
                                } else {
                                    resolutionAdditional.setVisibility(GONE);
                                }
                            }
                        });
                        break;
                    case 4:
                        // Adam Lynch and Harold Walsted checkbox
                        additionalCheckbox.setVisibility(VISIBLE);
                        additionalCheckbox.setText(R.string.adam_lynch_harold_walsted);
                        break;
                    case 5:
                        resignedOne.setText(defeated);
                        resignedTwo.setText(defeated);
                        resignedThree.setText(defeated);
                        resignedFour.setText(defeated);
                        // Engine car
                        globalVariables.EngineCar = 0;
                        additionalCheckbox.setVisibility(VISIBLE);
                        additionalCheckbox.setText(R.string.engine_car_dodge);
                        additionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (compoundButton.isChecked()) {
                                    additionalCheckboxTwo.setChecked(false);
                                    globalVariables.EngineCar = 1;
                                    selectInvestigatorHeading.setVisibility(VISIBLE);
                                    selectInvestigatorHeading.setText(R.string.engine_car_investigator);
                                    selectInvestigator.setVisibility(VISIBLE);
                                    switch (globalVariables.Investigators.size()) {
                                        case 4:
                                            selectInvestigatorFour.setVisibility(VISIBLE);
                                            selectInvestigatorFour.setText(investigatorNames[globalVariables
                                                    .Investigators.get(3)
                                                    .Name]);
                                            selectInvestigatorFour.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorThree.setChecked(false);
                                                        selectInvestigatorTwo.setChecked(false);
                                                        selectInvestigatorOne.setChecked(false);
                                                    }
                                                }
                                            });
                                        case 3:
                                            selectInvestigatorThree.setVisibility(VISIBLE);
                                            selectInvestigatorThree.setText(investigatorNames[globalVariables
                                                    .Investigators.get
                                                            (2).Name]);
                                            selectInvestigatorThree.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorFour.setChecked(false);
                                                        selectInvestigatorTwo.setChecked(false);
                                                        selectInvestigatorOne.setChecked(false);
                                                    }
                                                }
                                            });
                                        case 2:
                                            selectInvestigatorTwo.setVisibility(VISIBLE);
                                            selectInvestigatorTwo.setText(investigatorNames[globalVariables
                                                    .Investigators.get(1)
                                                    .Name]);
                                            selectInvestigatorTwo.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorFour.setChecked(false);
                                                        selectInvestigatorThree.setChecked(false);
                                                        selectInvestigatorOne.setChecked(false);
                                                    }
                                                }
                                            });
                                        case 1:
                                            selectInvestigatorOne.setVisibility(VISIBLE);
                                            selectInvestigatorOne.setText(investigatorNames[globalVariables
                                                    .Investigators.get(0)
                                                    .Name]);
                                            selectInvestigatorOne.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorFour.setChecked(false);
                                                        selectInvestigatorThree.setChecked(false);
                                                        selectInvestigatorTwo.setChecked(false);
                                                    }
                                                }
                                            });
                                    }
                                } else {
                                    globalVariables.EngineCar = 0;
                                    selectInvestigatorHeading.setVisibility(GONE);
                                    selectInvestigator.setVisibility(GONE);
                                }
                            }
                        });
                        additionalCheckboxTwo.setVisibility(VISIBLE);
                        additionalCheckboxTwo.setText(R.string.engine_car_endure);
                        additionalCheckboxTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (compoundButton.isChecked()) {
                                    additionalCheckbox.setChecked(false);
                                    globalVariables.EngineCar = 2;
                                    selectInvestigatorHeading.setVisibility(VISIBLE);
                                    selectInvestigatorHeading.setText(R.string.engine_car_investigator);
                                    selectInvestigator.setVisibility(VISIBLE);
                                    switch (globalVariables.Investigators.size()) {
                                        case 4:
                                            selectInvestigatorFour.setVisibility(VISIBLE);
                                            selectInvestigatorFour.setText(investigatorNames[globalVariables
                                                    .Investigators.get(3)
                                                    .Name]);
                                            selectInvestigatorFour.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorThree.setChecked(false);
                                                        selectInvestigatorTwo.setChecked(false);
                                                        selectInvestigatorOne.setChecked(false);
                                                    }
                                                }
                                            });
                                        case 3:
                                            selectInvestigatorThree.setVisibility(VISIBLE);
                                            selectInvestigatorThree.setText(investigatorNames[globalVariables
                                                    .Investigators.get
                                                            (2).Name]);
                                            selectInvestigatorThree.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorFour.setChecked(false);
                                                        selectInvestigatorTwo.setChecked(false);
                                                        selectInvestigatorOne.setChecked(false);
                                                    }
                                                }
                                            });
                                        case 2:
                                            selectInvestigatorTwo.setVisibility(VISIBLE);
                                            selectInvestigatorTwo.setText(investigatorNames[globalVariables
                                                    .Investigators.get(1)
                                                    .Name]);
                                            selectInvestigatorTwo.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorFour.setChecked(false);
                                                        selectInvestigatorThree.setChecked(false);
                                                        selectInvestigatorOne.setChecked(false);
                                                    }
                                                }
                                            });
                                        case 1:
                                            selectInvestigatorOne.setVisibility(VISIBLE);
                                            selectInvestigatorOne.setText(investigatorNames[globalVariables
                                                    .Investigators.get(0)
                                                    .Name]);
                                            selectInvestigatorOne.setOnCheckedChangeListener(new CompoundButton
                                                    .OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (compoundButton.isChecked()) {
                                                        selectInvestigatorFour.setChecked(false);
                                                        selectInvestigatorThree.setChecked(false);
                                                        selectInvestigatorTwo.setChecked(false);
                                                    }
                                                }
                                            });
                                    }
                                } else {
                                    globalVariables.EngineCar = 0;
                                    selectInvestigatorHeading.setVisibility(GONE);
                                    selectInvestigator.setVisibility(GONE);
                                }
                            }
                        });
                        break;
                    case 6:
                        // Sacrificed view
                        additionalGroupHeading.setVisibility(VISIBLE);
                        additionalGroupHeading.setText(R.string.underneath_agenda_deck);
                        additionalGroup.setVisibility(VISIBLE);
                        additionalGroupOne.setText(R.string.henry_armitage);
                        additionalGroupTwo.setText(R.string.warren_rice);
                        additionalGroupThree.setText(R.string.francis_morgan);
                        additionalGroupFour.setText(R.string.zebulon_whateley);
                        additionalGroupFive.setText(R.string.earl_sawyer);
                        additionalGroupSix.setText(R.string.another_ally);
                        break;
                    case 8:
                        globalVariables.BroodsEscaped = 0;
                        additionalCounterLayout.setVisibility(VISIBLE);
                        additionalCounter.setText(R.string.brood_escaped_counter);
                        additionalAmount.setText(String.valueOf(globalVariables.BroodsEscaped));
                        additionalDecrement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int current = globalVariables.BroodsEscaped;
                                if (current > 0) {
                                    globalVariables.BroodsEscaped += -1;
                                    additionalAmount.setText(String.valueOf(globalVariables.BroodsEscaped));
                                }
                            }
                        });
                        additionalIncrement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int current = globalVariables.BroodsEscaped;
                                if (current < 5) {
                                    globalVariables.BroodsEscaped += 1;
                                    additionalAmount.setText(String.valueOf(globalVariables.BroodsEscaped));
                                }
                            }
                        });
                        break;
                }
                break;
            case 3:
                break;
        }
        if (globalVariables.CurrentScenario > 100) {
            switch (globalVariables.CurrentScenario) {
                // Carnevale of Horrors
                case 102:
                    additionalCheckbox.setVisibility(VISIBLE);
                    additionalCheckboxTwo.setVisibility(VISIBLE);
                    additionalCheckbox.setText(R.string.carnevale_sacrifice);
                    additionalCheckboxTwo.setText(R.string.carnevale_abbess);
                    additionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (compoundButton.isChecked()) {
                                additionalCheckboxTwo.setChecked(false);
                                resolutionAdditional.setVisibility(VISIBLE);
                                resolutionAdditional.setText(R.string.carnevale_reward_sacrifice);
                            } else {
                                resolutionAdditional.setVisibility(GONE);
                            }
                        }
                    });
                    additionalCheckboxTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (compoundButton.isChecked()) {
                                additionalCheckbox.setChecked(false);
                                resolutionAdditional.setVisibility(VISIBLE);
                                resolutionAdditional.setText(R.string.carnevale_reward_abbess);
                            } else {
                                resolutionAdditional.setVisibility(GONE);
                            }
                        }
                    });
                    break;
            }
        }

        Button playerCardsButton = (Button) findViewById(R.id.player_cards_button);
        playerCardsButton.setTypeface(teutonic);
        playerCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment playerCardsDialog = new PlayerCardsDialog();
                playerCardsDialog.show(getFragmentManager(), "player_cards");
            }
        });

        Button weaknessButton = (Button) findViewById(R.id.investigator_weakness_button);
        weaknessButton.setTypeface(teutonic);
        boolean weakness = false;
        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
            switch (globalVariables.Investigators.get(i).Name) {
                case Investigator.ROLAND_BANKS:
                case Investigator.SKIDS_OTOOLE:
                case Investigator.ZOEY_SAMARAS:
                case Investigator.JENNY_BARNES:
                case Investigator.AKACHI_ONYELE:
                    weakness = true;
                    break;
            }
        }
        if (weakness) {
            weaknessButton.setVisibility(VISIBLE);
        } else {
            LinearLayout buttonBar = (LinearLayout) findViewById(R.id.button_bar);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)
                    getResources().getDimension(R.dimen.button_height));
            buttonBar.setLayoutParams(params);
        }
        weaknessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment weaknessDialog = new WeaknessDialog();
                weaknessDialog.show(getFragmentManager(), "weakness");
            }
        });

        Button logButton = (Button) findViewById(R.id.campaign_log_button);
        logButton.setTypeface(teutonic);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScenarioResolutionActivity.this, CampaignLogActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setTypeface(teutonic);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setTypeface(teutonic);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check scenario requirements have been met
                boolean dialog = true;
                // Make sure a resolution has been selected
                if (globalVariables.ScenarioResolution == -1) {
                    dialog = false;
                    Toast toast = Toast.makeText(ScenarioResolutionActivity.this, "You must select a " +
                            "resolution.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                switch (globalVariables.CurrentCampaign) {
                    case 1:
                        switch (globalVariables.CurrentScenario) {
                            case 1:
                                // Make sure a lead investigator is selected
                                if (!selectInvestigatorOne.isChecked() && !selectInvestigatorTwo.isChecked()
                                        && !selectInvestigatorThree.isChecked() && !selectInvestigatorFour
                                        .isChecked() && (globalVariables.ScenarioResolution == 2 || globalVariables
                                        .ScenarioResolution == 3)) {
                                    dialog = false;
                                    Toast toast = Toast.makeText(ScenarioResolutionActivity.this, "You must " +
                                            "select a " +
                                            "lead investigator.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                        }
                        break;
                    case 2:
                        switch (globalVariables.CurrentScenario) {
                            case 5:
                                if (globalVariables.EngineCar != 0 && !selectInvestigatorOne.isChecked() &&
                                        !selectInvestigatorTwo.isChecked()
                                        && !selectInvestigatorThree.isChecked() && !selectInvestigatorFour
                                        .isChecked()) {
                                    dialog = false;
                                    Toast toast = Toast.makeText(ScenarioResolutionActivity.this, "You must " +
                                            "select which investigator entered the engine car.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                            case 10:
                                if (globalVariables.ScenarioResolution == 0) {
                                    dialog = false;
                                    Toast toast = Toast.makeText(ScenarioResolutionActivity.this, "You must " +
                                            "select the Act when the scenario ended.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                        }
                        break;
                }
                // Open dialog if requirements are met
                if (dialog) {
                    DialogFragment finishScenarioDialog = new FinishScenarioDialog();
                    finishScenarioDialog.show(getFragmentManager(), "finish_scenario");
                }
            }
        });

        // If standalone scenario, hide all relevant views and set continue button to go back to main menu
        if(globalVariables.CurrentCampaign == 999){
            defeatedHeading.setVisibility(GONE);
            defeatedOne.setVisibility(GONE);
            defeatedTwo.setVisibility(GONE);
            defeatedThree.setVisibility(GONE);
            defeatedFour.setVisibility(GONE);
            LinearLayout buttonBar = (LinearLayout) findViewById(R.id.button_bar);
            buttonBar.setVisibility(GONE);
            logButton.setVisibility(GONE);
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ScenarioResolutionActivity.this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    /*
        Listener for the defeated checkboxes
     */
    private class InvestigatorDefeatedCheckboxListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton button, boolean b) {
            int investigator = -1;
            ViewGroup parent = (ViewGroup) button.getParent();

            switch (parent.getId()) {
                case R.id.defeated_one_selection:
                    investigator = 0;
                    break;
                case R.id.defeated_two_selection:
                    investigator = 1;
                    break;
                case R.id.defeated_three_selection:
                    investigator = 2;
                    break;
                case R.id.defeated_four_selection:
                    investigator = 3;
                    break;
            }

            if (!button.isChecked()) {
                globalVariables.Investigators.get(investigator).TempStatus = 0;
            } else {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox box = (CheckBox) view;
                        if (box != button) {
                            box.setChecked(false);
                        }
                    }
                }
                switch (button.getId()) {
                    case R.id.resigned_button_one:
                    case R.id.resigned_button_two:
                    case R.id.resigned_button_three:
                    case R.id.resigned_button_four:
                        globalVariables.Investigators.get(investigator).TempStatus = 1;
                        break;
                    case R.id.damage_button_one:
                    case R.id.damage_button_two:
                    case R.id.damage_button_three:
                    case R.id.damage_button_four:
                        globalVariables.Investigators.get(investigator).TempStatus = 2;
                        break;
                    case R.id.horror_button_one:
                    case R.id.horror_button_two:
                    case R.id.horror_button_three:
                    case R.id.horror_button_four:
                        globalVariables.Investigators.get(investigator).TempStatus = 3;
                        break;
                }
            }

            // Clear and reset the select resolution checkbox to refresh the resolution text
            RadioGroup selectResolution = (RadioGroup) findViewById(R.id.select_resolution);
            RadioButton noResolution = (RadioButton) findViewById(R.id.no_resolution);
            RadioButton resolutionOne = (RadioButton) findViewById(R.id.resolution_one);
            RadioButton resolutionTwo = (RadioButton) findViewById(R.id.resolution_two);
            RadioButton resolutionThree = (RadioButton) findViewById(R.id.resolution_three);
            RadioButton resolutionFour = (RadioButton) findViewById(R.id.resolution_four);
            if (globalVariables.ScenarioResolution != -1) {
                selectResolution.clearCheck();
                switch (globalVariables.ScenarioResolution) {
                    case 0:
                        noResolution.setChecked(true);
                        break;
                    case 1:
                        resolutionOne.setChecked(true);
                        break;
                    case 2:
                        resolutionTwo.setChecked(true);
                        break;
                    case 3:
                        resolutionThree.setChecked(true);
                        break;
                    case 4:
                        resolutionFour.setChecked(true);
                        break;
                }
            }
        }
    }

    /*
        Listener for the defeated images
     */
    private class InvestigatorDefeatedImageListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int investigator = -1;
            int buttonId = -1;
            ViewGroup parent = (ViewGroup) view.getParent();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View image = parent.getChildAt(i);
                if (view == image) {
                    buttonId = i - 1;
                }
            }
            CheckBox button = (CheckBox) parent.getChildAt(buttonId);

            switch (parent.getId()) {
                case R.id.defeated_one_selection:
                    investigator = 0;
                    break;
                case R.id.defeated_two_selection:
                    investigator = 1;
                    break;
                case R.id.defeated_three_selection:
                    investigator = 2;
                    break;
                case R.id.defeated_four_selection:
                    investigator = 3;
                    break;
            }

            if (button.isChecked()) {
                button.setChecked(false);
                globalVariables.Investigators.get(investigator).TempStatus = 0;
            } else {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View v = parent.getChildAt(i);
                    if (v instanceof CheckBox) {
                        CheckBox box = (CheckBox) v;
                        if (box != button) {
                            box.setChecked(false);
                        }
                    }
                }
                button.setChecked(true);
            }
        }
    }

    /*
        Listener to set the resolution text
     */
    private class ResolutionCheckboxListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            LinearLayout parent = (LinearLayout) radioGroup.getParent();
            final TextView resolutionTextView = (TextView) parent.findViewById(R.id.resolution_text);
            resolutionTextView.setVisibility(VISIBLE);
            final CheckBox additional = (CheckBox) findViewById(R.id.additional_checkbox_one);
            final CheckBox additionalTwo = (CheckBox) findViewById(R.id.additional_checkbox_two);
            final TextView selectInvestigatorHeading = (TextView) findViewById(R.id.select_investigator_heading);
            final LinearLayout selectInvestigator = (LinearLayout) findViewById(R.id.select_investigator);
            final CheckBox selectInvestigatorOne = (CheckBox) findViewById(R.id.select_investigator_one);
            final CheckBox selectInvestigatorTwo = (CheckBox) findViewById(R.id.select_investigator_two);
            final CheckBox selectInvestigatorThree = (CheckBox) findViewById(R.id.select_investigator_three);
            final CheckBox selectInvestigatorFour = (CheckBox) findViewById(R.id.select_investigator_four);

            // Set resolution
            switch (i) {
                case R.id.no_resolution:
                    globalVariables.ScenarioResolution = 0;
                    break;
                case R.id.resolution_one:
                    globalVariables.ScenarioResolution = 1;
                    break;
                case R.id.resolution_two:
                    globalVariables.ScenarioResolution = 2;
                    break;
                case R.id.resolution_three:
                    globalVariables.ScenarioResolution = 3;
                    break;
                case R.id.resolution_four:
                    globalVariables.ScenarioResolution = 4;
                    break;
            }

            // Set correct resolution text
            switch (globalVariables.CurrentCampaign) {
                // Night of the Zealot
                case 1:
                    switch (globalVariables.CurrentScenario) {
                        // The Gathering
                        case 1:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.gathering_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.gathering_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.gathering_resolution_two);
                                    break;
                                case 3:
                                    resolutionTextView.setText(R.string.gathering_resolution_three);
                                    break;
                            }
                            // Set up checkboxes to select lead investigator
                            if (globalVariables.ScenarioResolution == 2 || globalVariables.ScenarioResolution == 3) {
                                selectInvestigatorHeading.setVisibility(VISIBLE);
                                selectInvestigatorHeading.setText(R.string.select_lead_investigator);
                                selectInvestigator.setVisibility(VISIBLE);
                                String[] investigatorNames = getResources().getStringArray(R.array.investigators);
                                switch (globalVariables.Investigators.size()) {
                                    case 4:
                                        selectInvestigatorFour.setVisibility(VISIBLE);
                                        selectInvestigatorFour.setText(investigatorNames[globalVariables
                                                .Investigators.get(3)
                                                .Name]);
                                        selectInvestigatorFour.setOnCheckedChangeListener(new CompoundButton
                                                .OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                if (compoundButton.isChecked()) {
                                                    selectInvestigatorThree.setChecked(false);
                                                    selectInvestigatorTwo.setChecked(false);
                                                    selectInvestigatorOne.setChecked(false);
                                                }
                                            }
                                        });
                                    case 3:
                                        selectInvestigatorThree.setVisibility(VISIBLE);
                                        selectInvestigatorThree.setText(investigatorNames[globalVariables
                                                .Investigators.get
                                                        (2).Name]);
                                        selectInvestigatorThree.setOnCheckedChangeListener(new CompoundButton
                                                .OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                if (compoundButton.isChecked()) {
                                                    selectInvestigatorFour.setChecked(false);
                                                    selectInvestigatorTwo.setChecked(false);
                                                    selectInvestigatorOne.setChecked(false);
                                                }
                                            }
                                        });
                                    case 2:
                                        selectInvestigatorTwo.setVisibility(VISIBLE);
                                        selectInvestigatorTwo.setText(investigatorNames[globalVariables
                                                .Investigators.get(1)
                                                .Name]);
                                        selectInvestigatorTwo.setOnCheckedChangeListener(new CompoundButton
                                                .OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                if (compoundButton.isChecked()) {
                                                    selectInvestigatorFour.setChecked(false);
                                                    selectInvestigatorThree.setChecked(false);
                                                    selectInvestigatorOne.setChecked(false);
                                                }
                                            }
                                        });
                                    case 1:
                                        selectInvestigatorOne.setVisibility(VISIBLE);
                                        selectInvestigatorOne.setText(investigatorNames[globalVariables
                                                .Investigators.get(0)
                                                .Name]);
                                        selectInvestigatorOne.setOnCheckedChangeListener(new CompoundButton
                                                .OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                if (compoundButton.isChecked()) {
                                                    selectInvestigatorFour.setChecked(false);
                                                    selectInvestigatorThree.setChecked(false);
                                                    selectInvestigatorTwo.setChecked(false);
                                                }
                                            }
                                        });
                                        break;
                                }
                            } else {
                                selectInvestigatorHeading.setVisibility(GONE);
                                selectInvestigator.setVisibility(GONE);
                            }
                            break;
                        // Midnight Masks
                        case 2:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.midnight_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.midnight_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.midnight_resolution_two);
                                    break;
                            }
                            break;
                        // The Devourer Below
                        case 3:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.devourer_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.devourer_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.devourer_resolution_two);
                                    break;
                                case 3:
                                    resolutionTextView.setText(R.string.devourer_resolution_three);
                                    break;
                            }
                            break;
                    }
                    break;
                // The Dunwich Legacy
                case 2:
                    switch (globalVariables.CurrentScenario) {
                        // Extracurricular Activity
                        case 1:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.extracurricular_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.extracurricular_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.extracurricular_resolution_two);
                                    break;
                                case 3:
                                    resolutionTextView.setText(R.string.extracurricular_resolution_three);
                                    break;
                                case 4:
                                    resolutionTextView.setText(R.string.extracurricular_resolution_four);
                                    break;
                            }
                            break;
                        // House Always Wins
                        case 2:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.house_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.house_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.house_resolution_two);
                                    break;
                                case 3:
                                    resolutionTextView.setText(R.string.house_resolution_three);
                                    break;
                                case 4:
                                    resolutionTextView.setText(R.string.house_resolution_four);
                                    break;
                            }
                            break;
                        // Miskatonic Museum
                        case 4:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.miskatonic_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.miskatonic_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.miskatonic_resolution_two);
                                    break;
                            }
                            break;
                        // Essex County Express
                        case 5:
                            boolean defeated = false;
                            for (int a = 0; a < globalVariables.Investigators.size(); a++) {
                                if (globalVariables.Investigators.get(a).TempStatus > 0) {
                                    defeated = true;
                                }
                            }
                            if ((defeated || globalVariables.ScenarioResolution == 0 || globalVariables
                                    .ScenarioResolution == 2) && (globalVariables.Necronomicon == 2 ||
                                    globalVariables.HenryArmitage == 1 || globalVariables.FrancisMorgan == 1 ||
                                    globalVariables.WarrenRice == 1)) {
                                TextView additionalHeading = (TextView) findViewById(R.id
                                        .additional_group_heading);
                                additionalHeading.setVisibility(VISIBLE);
                                additionalHeading.setText(R.string.kidnapped_cards);
                                LinearLayout additionalGroup = (LinearLayout) findViewById(R.id
                                        .additional_group);
                                additionalGroup.setVisibility(VISIBLE);
                                LinearLayout additionalGroupSetTwo = (LinearLayout) findViewById(R.id
                                        .additional_group_set_two);
                                additionalGroupSetTwo.setVisibility(GONE);
                                final CheckBox additionalGroupOne = (CheckBox) findViewById(R.id
                                        .additional_group_one);
                                final CheckBox additionalGroupTwo = (CheckBox) findViewById(R.id
                                        .additional_group_two);
                                final CheckBox additionalGroupThree = (CheckBox) findViewById(R.id
                                        .additional_group_three);
                                final CheckBox additionalGroupSeven = (CheckBox) findViewById(R.id
                                        .additional_group_seven);
                                additionalGroupSeven.setVisibility(VISIBLE);
                                additionalGroupOne.setText(R.string.necronomicon_defeated);
                                additionalGroupTwo.setText(R.string.armitage_defeated);
                                additionalGroupThree.setText(R.string.rice_defeated);
                                additionalGroupSeven.setText(R.string.morgan_defeated);
                                class KidnappedCheckedChangeListener implements CompoundButton
                                        .OnCheckedChangeListener {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        final SpannableStringBuilder defeatedStringBuilder = new
                                                SpannableStringBuilder();
                                        if (additionalGroupOne.isChecked()) {
                                            defeatedStringBuilder.append(getString(R.string
                                                    .remove_necronomicon));
                                        }
                                        if (additionalGroupTwo.isChecked()) {
                                            defeatedStringBuilder.append(getString(R.string.remove_armitage));
                                        }
                                        if (additionalGroupThree.isChecked()) {
                                            defeatedStringBuilder.append(getString(R.string.remove_rice));
                                        }
                                        if (additionalGroupSeven.isChecked()) {
                                            defeatedStringBuilder.append(getString(R.string.remove_morgan));
                                        }
                                        if (additionalGroupOne.isChecked() || additionalGroupTwo.isChecked() ||
                                                additionalGroupThree.isChecked() || additionalGroupSeven
                                                .isChecked()) {
                                            String defeatedString = defeatedStringBuilder.toString();
                                            TextView additionalResolution = (TextView) findViewById(R.id
                                                    .resolution_text_additional);
                                            additionalResolution.setTypeface(additionalResolution.getTypeface(),
                                                    Typeface.BOLD);
                                            additionalResolution.setText(defeatedString.trim());
                                            additionalResolution.setVisibility(VISIBLE);
                                        } else {
                                            TextView additionalResolution = (TextView) findViewById(R.id
                                                    .resolution_text_additional);
                                            additionalResolution.setVisibility(GONE);
                                        }
                                    }
                                }
                                additionalGroupOne.setOnCheckedChangeListener(new
                                        KidnappedCheckedChangeListener());
                                additionalGroupTwo.setOnCheckedChangeListener(new
                                        KidnappedCheckedChangeListener());
                                additionalGroupThree.setOnCheckedChangeListener(new
                                        KidnappedCheckedChangeListener());
                                additionalGroupSeven.setOnCheckedChangeListener(new
                                        KidnappedCheckedChangeListener());
                                if (globalVariables.Necronomicon != 2) {
                                    additionalGroupOne.setVisibility(GONE);
                                }
                                if (globalVariables.HenryArmitage != 1) {
                                    additionalGroupTwo.setVisibility(GONE);
                                }
                                if (globalVariables.WarrenRice != 1) {
                                    additionalGroupThree.setVisibility(GONE);
                                }
                                if (globalVariables.FrancisMorgan != 1) {
                                    additionalGroupSeven.setVisibility(GONE);
                                }
                            }
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.essex_no_resolution);
                                    break;
                                case 1:
                                    if (defeated) {
                                        resolutionTextView.setText(R.string.essex_resolution_one_defeated);
                                    } else {
                                        resolutionTextView.setText(R.string.essex_resolution_one);
                                    }
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.essex_resolution_two);
                                    break;
                            }
                            break;
                        // Blood on the Altar
                        case 6:
                            defeated = false;
                            for (int a = 0; a < globalVariables.Investigators.size(); a++) {
                                if (globalVariables.Investigators.get(a).TempStatus > 1) {
                                    defeated = true;
                                }
                            }
                            TextView resolution = (TextView) findViewById(R.id.resolution_text_additional);
                            if (defeated && globalVariables.Necronomicon == 2 && globalVariables
                                    .ScenarioResolution
                                    != 2) {
                                additional.setVisibility(VISIBLE);
                                additional.setText(R.string.necronomicon_defeated);
                                resolution.setVisibility(VISIBLE);
                                resolution.setText(getString(R.string.remove_necronomicon).trim());
                            } else {
                                additional.setVisibility(GONE);
                            }
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.blood_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.blood_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.blood_resolution_two);
                                    break;
                                case 3:
                                    resolutionTextView.setText(R.string.blood_resolution_three);
                                    break;
                            }
                            break;
                        // Undimensioned and Unseen
                        case 8:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.undimensioned_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.undimensioned_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.undimensioned_resolution_two);
                                    break;
                            }
                            break;
                        // Where Doom Awaits
                        case 9:
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.doom_no_resolution);
                                    break;
                                case 1:
                                    resolutionTextView.setText(R.string.doom_resolution_one);
                                    break;
                                case 2:
                                    resolutionTextView.setText(R.string.doom_resolution_two);
                                    break;
                            }
                            break;
                        // Lost in Time and Space
                        case 10:
                            defeated = false;
                            for (int a = 0; a < globalVariables.Investigators.size(); a++) {
                                if (globalVariables.Investigators.get(a).TempStatus > 1) {
                                    defeated = true;
                                }
                            }
                            switch (globalVariables.ScenarioResolution) {
                                case 0:
                                    resolutionTextView.setText(R.string.lost_no_resolution);
                                    additional.setVisibility(VISIBLE);
                                    additional.setText(R.string.act_one_two_three);
                                    additional.setOnCheckedChangeListener(new CompoundButton
                                            .OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            if (compoundButton.isChecked()) {
                                                additionalTwo.setChecked(false);
                                                globalVariables.ScenarioResolution = 4;
                                                resolutionTextView.setText(R.string.lost_resolution_four);
                                            } else {
                                                globalVariables.ScenarioResolution = 0;
                                                resolutionTextView.setText(R.string.lost_no_resolution);
                                            }
                                        }
                                    });
                                    additionalTwo.setVisibility(VISIBLE);
                                    additionalTwo.setText(R.string.act_four);
                                    additionalTwo.setOnCheckedChangeListener(new CompoundButton
                                            .OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            if (compoundButton.isChecked()) {
                                                additional.setChecked(false);
                                                globalVariables.ScenarioResolution = 2;
                                                resolutionTextView.setText(R.string.lost_resolution_two);
                                            } else {
                                                globalVariables.ScenarioResolution = 0;
                                                resolutionTextView.setText(R.string.lost_no_resolution);
                                            }
                                        }
                                    });
                                    break;
                                case 1:
                                    if (defeated) {
                                        resolutionTextView.setText(R.string.lost_resolution_one_defeated);
                                    } else {
                                        resolutionTextView.setText(R.string.lost_resolution_one);
                                    }
                                    break;
                                case 3:
                                    if (defeated) {
                                        resolutionTextView.setText(R.string.lost_resolution_three_defeated);
                                    } else {
                                        resolutionTextView.setText(R.string.lost_resolution_three);
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
                case 3:
                    break;
            }
            if (globalVariables.CurrentScenario > 100) {
                switch (globalVariables.CurrentScenario) {
                    // Curse of the Rougarou
                    case 101:
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                resolutionTextView.setText(R.string.rougarou_no_resolution);
                                break;
                            case 1:
                                resolutionTextView.setText(R.string.rougarou_resolution_one);
                                break;
                            case 2:
                                resolutionTextView.setText(R.string.rougarou_resolution_two);
                                break;
                            case 3:
                                resolutionTextView.setText(R.string.rougarou_resolution_three);
                                break;
                        }
                        break;
                    // Carnevale of Horrors
                    case 102:
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                resolutionTextView.setText(R.string.carnevale_no_resolution);
                                break;
                            case 1:
                                resolutionTextView.setText(R.string.carnevale_resolution_one);
                                break;
                            case 2:
                                resolutionTextView.setText(R.string.carnevale_resolution_two);
                                break;
                        }
                        break;
                }
            }
        }
    }

    /*
        Dialog to finish the scenario
      */
    public static class FinishScenarioDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater and inflate the view
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.c_dialog_finish_scenario, null);

            Typeface teutonic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/teutonic.ttf");
            Typeface arnopro = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arnopro.otf");
            Typeface arnoprobold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arnoprobold.otf");
            TextView title = (TextView) v.findViewById(R.id.current_scenario_name);
            TextView confirm = (TextView) v.findViewById(R.id.confirm_finish_scenario);
            Button cancelButton = (Button) v.findViewById(R.id.cancel_button);
            Button okayButton = (Button) v.findViewById(R.id.okay_button);
            title.setTypeface(teutonic);
            confirm.setTypeface(arnoprobold);
            cancelButton.setTypeface(teutonic);
            okayButton.setTypeface(teutonic);

            // Set title
            switch (globalVariables.CurrentCampaign) {
                case 1:
                    switch (globalVariables.CurrentScenario) {
                        case 1:
                            title.setText(R.string.night_scenario_one);
                            break;
                        case 2:
                            title.setText(R.string.night_scenario_two);
                            break;
                        case 3:
                            title.setText(R.string.night_scenario_three);
                            break;
                    }
                    break;
                case 2:
                    switch (globalVariables.CurrentScenario) {
                        case 1:
                            title.setText(R.string.dunwich_scenario_one);
                            break;
                        case 2:
                            title.setText(R.string.dunwich_scenario_two);
                            break;
                        case 3:
                            title.setText(R.string.dunwich_interlude_one);
                            break;
                        case 4:
                            title.setText(R.string.dunwich_scenario_three);
                            break;
                        case 5:
                            title.setText(R.string.dunwich_scenario_four);
                            break;
                        case 6:
                            title.setText(R.string.dunwich_scenario_five);
                            break;
                        case 7:
                            title.setText(R.string.dunwich_interlude_two);
                            break;
                        case 8:
                            title.setText(R.string.dunwich_scenario_six);
                            break;
                        case 9:
                            title.setText(R.string.dunwich_scenario_seven);
                            break;
                        case 10:
                            title.setText(R.string.dunwich_scenario_eight);
                            break;
                        case 11:
                            title.setText(R.string.dunwich_epilogue);
                            break;
                    }
                    break;
                case 3:
                    break;
            }
            if (globalVariables.CurrentScenario > 100) {
                switch (globalVariables.CurrentScenario) {
                    case 101:
                        title.setText(R.string.rougarou_scenario);
                        break;
                    case 102:
                        title.setText(R.string.carnevale_scenario);
                        break;
                }
            }

            // Set resolution number and victory display
            TextView resolution = (TextView) v.findViewById(R.id.current_resolution);
            TextView victory = (TextView) v.findViewById(R.id.current_victory_display);
            resolution.setTypeface(arnopro);
            victory.setTypeface(arnopro);
            switch (globalVariables.ScenarioResolution) {
                case 0:
                    resolution.setText(R.string.no_resolution);
                    break;
                case 1:
                    resolution.setText(R.string.resolution_one);
                    break;
                case 2:
                    resolution.setText(R.string.resolution_two);
                    break;
                case 3:
                    resolution.setText(R.string.resolution_three);
                    break;
                case 4:
                    resolution.setText(R.string.resolution_four);
                    break;
            }
            String victoryText = getString(R.string.victory_display) + " " + Integer.toString(globalVariables
                    .VictoryDisplay);
            victory.setText(victoryText);

            /*
                Show the right views for the number of investigators and set the right font to the name
             */
            LinearLayout investigatorOne = (LinearLayout) v.findViewById(R.id.investigator_one);
            LinearLayout investigatorTwo = (LinearLayout) v.findViewById(R.id.investigator_two);
            LinearLayout investigatorThree = (LinearLayout) v.findViewById(R.id.investigator_three);
            LinearLayout investigatorFour = (LinearLayout) v.findViewById(R.id.investigator_four);
            TextView investigatorOneName = (TextView) v.findViewById(R.id.investigator_one_name);
            TextView investigatorTwoName = (TextView) v.findViewById(R.id.investigator_two_name);
            TextView investigatorThreeName = (TextView) v.findViewById(R.id.investigator_three_name);
            TextView investigatorFourName = (TextView) v.findViewById(R.id.investigator_four_name);
            TextView investigatorOneStatus = (TextView) v.findViewById(R.id.investigator_one_status);
            TextView investigatorTwoStatus = (TextView) v.findViewById(R.id.investigator_two_status);
            TextView investigatorThreeStatus = (TextView) v.findViewById(R.id.investigator_three_status);
            TextView investigatorFourStatus = (TextView) v.findViewById(R.id.investigator_four_status);
            String[] investigatorNames = getResources().getStringArray(R.array.investigators);
            // For each investigator, set it visible or not, apply the right name to it, set the right typeface and
            // set a listener to the link
            switch (globalVariables.Investigators.size()) {
                case 4:
                    investigatorFour.setVisibility(VISIBLE);
                    investigatorFourName.setText(investigatorNames[globalVariables.Investigators.get(3).Name]);
                    investigatorFourName.setTypeface(arnoprobold);
                    investigatorFourStatus.setTypeface(arnopro);
                    switch (globalVariables.Investigators.get(3).TempStatus) {
                        case 0:
                            investigatorFourStatus.setText(R.string.not_defeated);
                            break;
                        case 1:
                            investigatorFourStatus.setText(R.string.resigned);
                            break;
                        case 2:
                            investigatorFourStatus.setText(R.string.defeated_damage);
                            break;
                        case 3:
                            investigatorFourStatus.setText(R.string.defeated_horror);
                            break;
                    }
                case 3:
                    investigatorThree.setVisibility(VISIBLE);
                    investigatorThreeName.setText(investigatorNames[globalVariables.Investigators.get(2).Name]);
                    investigatorThreeName.setTypeface(arnoprobold);
                    investigatorThreeStatus.setTypeface(arnopro);
                    switch (globalVariables.Investigators.get(2).TempStatus) {
                        case 0:
                            investigatorThreeStatus.setText(R.string.not_defeated);
                            break;
                        case 1:
                            investigatorThreeStatus.setText(R.string.resigned);
                            break;
                        case 2:
                            investigatorThreeStatus.setText(R.string.defeated_damage);
                            break;
                        case 3:
                            investigatorThreeStatus.setText(R.string.defeated_horror);
                            break;
                    }
                case 2:
                    investigatorTwo.setVisibility(VISIBLE);
                    investigatorTwoName.setText(investigatorNames[globalVariables.Investigators.get(1).Name]);
                    investigatorTwoName.setTypeface(arnoprobold);
                    investigatorTwoStatus.setTypeface(arnopro);
                    switch (globalVariables.Investigators.get(1).TempStatus) {
                        case 0:
                            investigatorTwoStatus.setText(R.string.not_defeated);
                            break;
                        case 1:
                            investigatorTwoStatus.setText(R.string.resigned);
                            break;
                        case 2:
                            investigatorTwoStatus.setText(R.string.defeated_damage);
                            break;
                        case 3:
                            investigatorTwoStatus.setText(R.string.defeated_horror);
                            break;
                    }
                case 1:
                    investigatorOne.setVisibility(VISIBLE);
                    investigatorOneName.setText(investigatorNames[globalVariables.Investigators.get(0).Name]);
                    investigatorOneName.setTypeface(arnoprobold);
                    investigatorOneStatus.setTypeface(arnopro);
                    switch (globalVariables.Investigators.get(0).TempStatus) {
                        case 0:
                            investigatorOneStatus.setText(R.string.not_defeated);
                            break;
                        case 1:
                            investigatorOneStatus.setText(R.string.resigned);
                            break;
                        case 2:
                            investigatorOneStatus.setText(R.string.defeated_damage);
                            break;
                        case 3:
                            investigatorOneStatus.setText(R.string.defeated_horror);
                            break;
                    }
            }

            okayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Apply scenario resolutions
                    scenarioResolutions(getActivity());

                    // Go to the right next screen (investigators, interlude, campaign finish)
                    Intent intent = new Intent(getActivity(), ScenarioMainActivity.class);
                    // If all investigators are dead, go to campaign finish screen
                    int dead = 0;
                    for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                        if (globalVariables.Investigators.get(i).Status == 2) {
                            dead++;
                        }
                    }
                    if (dead == globalVariables.Investigators.size()) {
                        String sharedPrefs = getActivity().getResources().getString(R.string.expansions_owned);
                        String dunwichOwnedString = getActivity().getResources().getString(R.string
                                .dunwich_campaign);
                        String carcosaOwnedString = getActivity().getResources().getString(R.string
                                .carcosa_campaign);
                        SharedPreferences settings = getActivity().getSharedPreferences(sharedPrefs, 0);
                        boolean dunwichOwned = settings.getBoolean(dunwichOwnedString, true);
                        boolean carcosaOwned = settings.getBoolean(carcosaOwnedString, true);
                        int investigators = 5;
                        for (int a = 1; a <= 5; a++) {
                            if (globalVariables.InvestigatorsInUse[a] == 1) {
                                investigators--;
                            }
                        }
                        if (dunwichOwned) {
                            investigators += 5;
                            for (int a = 6; a <= 10; a++) {
                                if (globalVariables.InvestigatorsInUse[a] == 1) {
                                    investigators--;
                                }
                            }
                        }
                        if (carcosaOwned) {
                            investigators += 6;
                            for (int a = 11; a <= 16; a++) {
                                if (globalVariables.InvestigatorsInUse[a] == 1) {
                                    investigators--;
                                }
                            }
                        }
                        if (investigators == 0) {
                            intent = new Intent(getActivity(), ScenarioMainActivity.class);
                            startActivity(intent);
                            dismiss();
                        }
                    }
                    // Otherwise go to the relevant screen for the scenario (default is next scenario)
                    switch (globalVariables.CurrentCampaign) {
                        case 1:
                            switch (globalVariables.CurrentScenario) {
                                case 4:
                                    intent = new Intent(getActivity(), CampaignFinishedActivity.class);
                                    break;
                            }
                            break;
                        case 2:
                            switch (globalVariables.CurrentScenario) {
                                case 3:
                                    intent = new Intent(getActivity(), ScenarioInterludeActivity.class);
                                    break;
                                case 7:
                                    intent = new Intent(getActivity(), ScenarioInterludeActivity.class);
                                    break;
                                case 11:
                                    intent = new Intent(getActivity(), ScenarioInterludeActivity.class);
                                    break;
                                case 12:
                                    intent = new Intent(getActivity(), CampaignFinishedActivity.class);
                                    break;
                            }
                            break;
                        case 3:
                            break;
                    }
                    startActivity(intent);
                    dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            builder.setView(v);
            return builder.create();
        }

    }

    /*
        Method on continuing to apply the scenario resolution
     */
    private static void scenarioResolutions(Activity activity) {
        // Get all the relevant checkboxes and views
        CheckBox additionalCheckbox = (CheckBox) activity.findViewById(R.id.additional_checkbox_one);
        CheckBox additionalCheckboxTwo = (CheckBox) activity.findViewById(R.id.additional_checkbox_two);
        CheckBox investigatorOne = (CheckBox) activity.findViewById(R.id.select_investigator_one);
        CheckBox investigatorTwo = (CheckBox) activity.findViewById(R.id.select_investigator_two);
        CheckBox investigatorThree = (CheckBox) activity.findViewById(R.id.select_investigator_three);
        CheckBox investigatorFour = (CheckBox) activity.findViewById(R.id.select_investigator_four);
        CheckBox additionalOne = (CheckBox) activity.findViewById(R.id.additional_group_one);
        CheckBox additionalTwo = (CheckBox) activity.findViewById(R.id.additional_group_two);
        CheckBox additionalThree = (CheckBox) activity.findViewById(R.id.additional_group_three);
        CheckBox additionalFour = (CheckBox) activity.findViewById(R.id.additional_group_four);
        CheckBox additionalFive = (CheckBox) activity.findViewById(R.id.additional_group_five);
        CheckBox additionalSix = (CheckBox) activity.findViewById(R.id.additional_group_six);
        CheckBox additionalSeven = (CheckBox) activity.findViewById(R.id.additional_group_seven);

        // Apply resolutions
        switch (globalVariables.CurrentCampaign) {

            // Night of the Zealot
            case 1:
                switch (globalVariables.CurrentScenario) {

                    // Night of the Zealot
                    case 1:
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                globalVariables.HouseStanding = 1;
                                globalVariables.GhoulPriest = 1;
                                globalVariables.LitaChantler = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    Investigator current = globalVariables.Investigators.get(i);
                                    if (current.TempStatus == 0) {
                                        current.Damage += 1;
                                    }
                                    current.AvailableXP += (globalVariables.VictoryDisplay + 2);
                                }
                                break;
                            case 1:
                                globalVariables.HouseStanding = 0;
                                globalVariables.GhoulPriest = 0;
                                globalVariables.LitaChantler = 1;
                                if (investigatorOne.isChecked()) {
                                    globalVariables.Investigators.get(0).Horror += 1;
                                } else if (investigatorTwo.isChecked()) {
                                    globalVariables.Investigators.get(1).Horror += 1;
                                } else if (investigatorThree.isChecked()) {
                                    globalVariables.Investigators.get(2).Horror += 1;
                                } else if (investigatorFour.isChecked()) {
                                    globalVariables.Investigators.get(3).Horror += 1;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 2);
                                }
                                break;
                            case 2:
                                globalVariables.HouseStanding = 1;
                                globalVariables.GhoulPriest = 0;
                                globalVariables.LitaChantler = 0;
                                if (investigatorOne.isChecked()) {
                                    globalVariables.Investigators.get(0).AvailableXP += 1;
                                } else if (investigatorTwo.isChecked()) {
                                    globalVariables.Investigators.get(1).AvailableXP += 1;
                                } else if (investigatorThree.isChecked()) {
                                    globalVariables.Investigators.get(2).AvailableXP += 1;
                                } else if (investigatorFour.isChecked()) {
                                    globalVariables.Investigators.get(3).AvailableXP += 1;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 2);
                                }
                                break;
                            case 3:
                                globalVariables.HouseStanding = 1;
                                globalVariables.GhoulPriest = 1;
                                globalVariables.LitaChantler = 2;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    if (globalVariables.Investigators.get(i).TempStatus != 1) {
                                        globalVariables.Investigators.get(i).Status = 2;
                                    }
                                }
                                break;
                        }
                        break;

                    // The Midnight Masks
                    case 2:
                        if (globalVariables.GhoulPriest == 1) {
                            if (additionalCheckbox.isChecked()) {
                                globalVariables.GhoulPriest = 0;
                            }
                        }
                        globalVariables.CultistsInterrogated = 0;
                        if (additionalOne.isChecked()) {
                            globalVariables.DrewInterrogated = 1;
                            globalVariables.CultistsInterrogated += 1;
                        } else {
                            globalVariables.DrewInterrogated = 0;
                        }
                        if (additionalTwo.isChecked()) {
                            globalVariables.PeterInterrogated = 1;
                            globalVariables.CultistsInterrogated += 1;
                        } else {
                            globalVariables.PeterInterrogated = 0;
                        }
                        if (additionalThree.isChecked()) {
                            globalVariables.RuthInterrogated = 1;
                            globalVariables.CultistsInterrogated += 1;
                        } else {
                            globalVariables.RuthInterrogated = 0;
                        }
                        if (additionalFour.isChecked()) {
                            globalVariables.HermanInterrogated = 1;
                            globalVariables.CultistsInterrogated += 1;
                        } else {
                            globalVariables.HermanInterrogated = 0;
                        }
                        if (additionalFive.isChecked()) {
                            globalVariables.VictoriaInterrogated = 1;
                            globalVariables.CultistsInterrogated += 1;
                        } else {
                            globalVariables.VictoriaInterrogated = 0;
                        }
                        if (additionalSix.isChecked()) {
                            globalVariables.MaskedInterrogated = 1;
                            globalVariables.CultistsInterrogated += 1;
                        } else {
                            globalVariables.MaskedInterrogated = 0;
                        }
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                            case 1:
                                globalVariables.PastMidnight = 0;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 2:
                                globalVariables.PastMidnight = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                        }
                        break;

                    // The Devourer Below
                    case 3:
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                globalVariables.Umordhoth = 0;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Status = 2;
                                }
                                break;
                            case 1:
                                globalVariables.Umordhoth = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Horror += 2;
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 5);
                                }
                                break;
                            case 2:
                                globalVariables.Umordhoth = 2;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Damage += 2;
                                    globalVariables.Investigators.get(i).Horror += 2;
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 10);
                                }
                                break;
                            case 3:
                                globalVariables.Umordhoth = 3;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Damage += 2;
                                    globalVariables.Investigators.get(i).Horror += 2;
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                        }
                        globalVariables.NightCompleted = 1;
                        break;
                }
                break;
            // The Dunwich Legacy
            case 2:

                switch (globalVariables.CurrentScenario) {

                    // Extracurricular Activity
                    case 1:
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                globalVariables.WarrenRice = 0;
                                globalVariables.Students = 0;
                                if (globalVariables.FirstScenario == 1) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 1);
                                }
                                break;
                            case 1:
                                globalVariables.WarrenRice = 1;
                                globalVariables.Students = 0;
                                if (globalVariables.FirstScenario == 1) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 2:
                                globalVariables.WarrenRice = 0;
                                globalVariables.Students = 1;
                                if (globalVariables.FirstScenario == 1) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 3:
                                globalVariables.WarrenRice = 0;
                                globalVariables.Students = 2;
                                if (globalVariables.FirstScenario == 1) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 4:
                                globalVariables.WarrenRice = 0;
                                globalVariables.Students = 0;
                                globalVariables.InvestigatorsUnconscious = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 1);
                                }
                                break;
                        }
                        break;

                    // The House Always Wins
                    case 2:
                        if (additionalCheckbox.isChecked()) {
                            globalVariables.InvestigatorsCheated = 1;
                        } else {
                            globalVariables.InvestigatorsCheated = 0;
                        }
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                            case 1:
                                globalVariables.ObannionGang = 0;
                                globalVariables.FrancisMorgan = 0;
                                if (globalVariables.FirstScenario == 2) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 1);
                                }
                                break;
                            case 2:
                                globalVariables.ObannionGang = 0;
                                globalVariables.FrancisMorgan = 1;
                                if (globalVariables.FirstScenario == 2) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 3:
                                globalVariables.ObannionGang = 1;
                                globalVariables.FrancisMorgan = 0;
                                if (globalVariables.FirstScenario == 2) {
                                    globalVariables.InvestigatorsUnconscious = 0;
                                }
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 4:
                                globalVariables.ObannionGang = 0;
                                globalVariables.FrancisMorgan = 0;
                                globalVariables.InvestigatorsUnconscious = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 1);
                                    globalVariables.Investigators.get(i).Damage += 1;
                                }
                                break;
                        }
                        break;

                    // The Miskatonic Museum
                    case 4:
                        if (globalVariables.ScenarioResolution == 0) {
                            for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                if (globalVariables.Investigators.get(i).TempStatus == 0) {
                                    globalVariables.Investigators.get(i).Damage += 1;
                                }
                            }
                        }
                        globalVariables.Necronomicon = globalVariables.ScenarioResolution;
                        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                            globalVariables.Investigators.get(i).AvailableXP += globalVariables.VictoryDisplay;
                        }
                        break;

                    // The Essex County Express
                    case 5:
                        if (globalVariables.EngineCar == 1) {
                            if (investigatorOne.isChecked()) {
                                globalVariables.Investigators.get(0).Horror += 1;
                            } else if (investigatorTwo.isChecked()) {
                                globalVariables.Investigators.get(1).Horror += 1;
                            } else if (investigatorThree.isChecked()) {
                                globalVariables.Investigators.get(2).Horror += 1;
                            } else if (investigatorFour.isChecked()) {
                                globalVariables.Investigators.get(3).Horror += 1;
                            }
                        } else if (globalVariables.EngineCar == 2) {
                            if (investigatorOne.isChecked()) {
                                globalVariables.Investigators.get(0).Damage += 1;
                            } else if (investigatorTwo.isChecked()) {
                                globalVariables.Investigators.get(1).Damage += 1;
                            } else if (investigatorThree.isChecked()) {
                                globalVariables.Investigators.get(2).Damage += 1;
                            } else if (investigatorFour.isChecked()) {
                                globalVariables.Investigators.get(3).Damage += 1;
                            }
                        }
                        if (additionalOne.isChecked()) {
                            globalVariables.Necronomicon = 3;
                        }
                        if (additionalTwo.isChecked()) {
                            globalVariables.HenryArmitage = 0;
                        }
                        if (additionalThree.isChecked()) {
                            globalVariables.WarrenRice = 0;
                        }
                        if (additionalSeven.isChecked()) {
                            globalVariables.FrancisMorgan = 0;
                        }
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                globalVariables.InvestigatorsDelayed = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 1);
                                }
                                break;
                            case 1:
                                globalVariables.InvestigatorsDelayed = 0;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    if (globalVariables.Investigators.get(i).Status != 0) {
                                        globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                                .VictoryDisplay + 1);
                                    } else {
                                        globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                                .VictoryDisplay);
                                    }
                                }
                                break;
                            case 2:
                                globalVariables.InvestigatorsDelayed = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 1);
                                    if (globalVariables.Investigators.get(i).Status == 0) {
                                        globalVariables.Investigators.get(i).Horror += 1;
                                    }
                                }
                                break;
                        }
                        break;

                    // Blood on the Altar
                    case 6:
                        globalVariables.SilasBishop = globalVariables.ScenarioResolution;
                        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                            globalVariables.Investigators.get(i).AvailableXP += (globalVariables.VictoryDisplay +
                                    2);
                        }
                        if (additionalCheckbox.isChecked()) {
                            globalVariables.Necronomicon = 3;
                        }
                        if (additionalOne.isChecked()) {
                            globalVariables.HenryArmitage = 2;
                        } else {
                            globalVariables.HenryArmitage = 3;
                        }
                        if (additionalTwo.isChecked()) {
                            globalVariables.WarrenRice = 2;
                        } else {
                            globalVariables.WarrenRice = 3;
                        }
                        if (additionalThree.isChecked()) {
                            globalVariables.FrancisMorgan = 2;
                        } else {
                            globalVariables.FrancisMorgan = 3;
                        }
                        if (additionalFour.isChecked()) {
                            globalVariables.ZebulonWhateley = 1;
                        } else {
                            globalVariables.ZebulonWhateley = 0;
                        }
                        if (additionalFive.isChecked()) {
                            globalVariables.EarlSawyer = 1;
                        } else {
                            globalVariables.EarlSawyer = 0;
                        }
                        if (additionalSix.isChecked()) {
                            globalVariables.AllySacrificed = 1;
                        } else {
                            globalVariables.AllySacrificed = 0;
                        }
                        break;

                    // Undimensioned and Unseen
                    case 8:
                        if (globalVariables.ScenarioResolution == 2) {
                            globalVariables.BroodsEscaped = 0;
                        }
                        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                            globalVariables.Investigators.get(i).AvailableXP += globalVariables.VictoryDisplay;
                        }
                        break;

                    // Where Doom Awaits
                    case 9:
                        switch (globalVariables.ScenarioResolution) {
                            case 0:
                                globalVariables.InvestigatorsGate = 2;
                                globalVariables.DunwichCompleted = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Status = 2;
                                    globalVariables.Investigators.get(i).Horror = globalVariables
                                            .Investigators.get(i).Sanity;
                                }
                                break;
                            case 1:
                                globalVariables.InvestigatorsGate = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).AvailableXP += globalVariables
                                            .VictoryDisplay;
                                }
                                break;
                            case 2:
                                globalVariables.InvestigatorsGate = 2;
                                globalVariables.DunwichCompleted = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Status = 2;
                                    globalVariables.Investigators.get(i).Horror = globalVariables
                                            .Investigators.get(i).Sanity;
                                }
                                break;
                        }
                        break;

                    // Lost in Time and Space
                    case 10:
                        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                            if (globalVariables.Investigators.get(i).TempStatus > 1) {
                                globalVariables.Investigators.get(i).Status = 2;
                            }
                        }
                        switch (globalVariables.ScenarioResolution) {
                            case 1:
                                globalVariables.YogSothoth = 1;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Damage += 2;
                                    globalVariables.Investigators.get(i).Horror += 2;
                                    globalVariables.Investigators.get(i).AvailableXP += (globalVariables
                                            .VictoryDisplay + 5);
                                }
                                break;
                            case 2:
                                globalVariables.YogSothoth = 0;
                                break;
                            case 3:
                                globalVariables.YogSothoth = 2;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Status = 2;
                                }
                                break;
                            case 4:
                                globalVariables.YogSothoth = 3;
                                for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                                    globalVariables.Investigators.get(i).Status = 2;
                                    globalVariables.Investigators.get(i).Horror = globalVariables
                                            .Investigators.get(i).Sanity;
                                }
                                globalVariables.DunwichCompleted = 1;
                                break;
                        }
                        break;
                }
                break;
            // The Path to Carcosa
            case 3:
                break;
        }
        if (globalVariables.CurrentScenario > 100) {
            switch (globalVariables.CurrentScenario) {
                // Curse of the Rougarou
                case 101:
                    for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                        globalVariables.Investigators.get(i).AvailableXP += globalVariables.VictoryDisplay;
                    }
                    switch (globalVariables.ScenarioResolution) {
                        case 0:
                            globalVariables.Rougarou = 1;
                            break;
                        case 1:
                            globalVariables.Rougarou = 1;
                            break;
                        case 2:
                            globalVariables.Rougarou = 2;
                            break;
                        case 3:
                            globalVariables.Rougarou = 3;
                            break;
                    }
                    break;
                // Carnevale of Horrors
                case 102:
                    for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                        globalVariables.Investigators.get(i).AvailableXP += globalVariables.VictoryDisplay;
                    }
                    globalVariables.Carnevale = (globalVariables.ScenarioResolution + 1);
                    if (additionalCheckbox.isChecked()) {
                        globalVariables.CarnevaleReward = 1;
                    } else if (additionalCheckboxTwo.isChecked()) {
                        globalVariables.CarnevaleReward = 2;
                    } else {
                        globalVariables.CarnevaleReward = 0;
                    }
                    break;
            }
        }

        // Apply defeats from temp status and weaknesses and subtract spent XP
        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
            Investigator currentInvestigator = globalVariables.Investigators.get(i);

            currentInvestigator.AvailableXP += -currentInvestigator.TempXP;
            currentInvestigator.TempXP = 0;

            int status = currentInvestigator.TempStatus;
            // Add to physical trauma
            if (status == 2) {
                currentInvestigator.Damage += 1;
            }
            // Add to mental trauma
            else if (status == 3) {
                currentInvestigator.Horror += 1;
            }

            // Reset temp status
            currentInvestigator.TempStatus = 0;

            // Apply any relevant weaknesses
            if (currentInvestigator.TempWeakness == 1) {
                switch (currentInvestigator.Name) {
                    case Investigator.AKACHI_ONYELE:
                        currentInvestigator.Damage += 1;
                        break;
                    case Investigator.ROLAND_BANKS:
                    case Investigator.ZOEY_SAMARAS:
                    case Investigator.JENNY_BARNES:
                        currentInvestigator.Horror += 1;
                        break;
                    case Investigator.SKIDS_OTOOLE:
                        currentInvestigator.AvailableXP += -2;
                        for (; currentInvestigator.AvailableXP < 0; ) {
                            currentInvestigator.AvailableXP += 1;
                        }
                        break;
                }
                currentInvestigator.TempWeakness = 0;
            }

            // Check health and sanity
            if ((currentInvestigator.Damage >= currentInvestigator.Health) ||
                    (currentInvestigator.Horror >= currentInvestigator.Sanity)) {
                currentInvestigator.Status = 2;
            }
        }

        // Increment current scenario
        int nextScenario;
        if (globalVariables.CurrentCampaign == 2 && globalVariables.FirstScenario == 2) {
            if (globalVariables.CurrentScenario == 1) {
                nextScenario = 3;
            } else if (globalVariables.CurrentScenario == 2) {
                nextScenario = 1;
            } else {
                nextScenario = globalVariables.CurrentScenario + 1;
            }
        } else {
            nextScenario = globalVariables.CurrentScenario + 1;
        }
        if (globalVariables.CurrentScenario > 100) {
            nextScenario = globalVariables.PreviousScenario;
        }
        if (globalVariables.CurrentCampaign == 2 && globalVariables.DunwichCompleted == 1) {
            nextScenario = 12;
        }
        globalVariables.CurrentScenario = nextScenario;

        // Reset victory display
        globalVariables.VictoryDisplay = 0;

        saveCampaign(activity, globalVariables);
    }

    /*
        Save the campaign to the database
      */
    public static void saveCampaign(Context context, GlobalVariables globalVariables) {

        // Get a writable database
        ArkhamDbHelper mDbHelper = new ArkhamDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Update campaign variables (currently only the scenario number and which investigators are in use)
        ContentValues campaignValues = new ContentValues();
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_CURRENT_CAMPAIGN, globalVariables.CurrentCampaign);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_CURRENT_SCENARIO, globalVariables.CurrentScenario);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_DIFFICULTY, globalVariables.CurrentDifficulty);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_NIGHT_COMPLETED, globalVariables
                .NightCompleted);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_DUNWICH_COMPLETED, globalVariables
                .DunwichCompleted);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_ROLAND_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.ROLAND_BANKS]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_DAISY_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.DAISY_WALKER]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_SKIDS_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.SKIDS_OTOOLE]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_AGNES_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.AGNES_BAKER]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_WENDY_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.WENDY_ADAMS]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_ZOEY_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.ZOEY_SAMARAS]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_REX_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.REX_MURPHY]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_JENNY_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.JENNY_BARNES]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_JIM_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.JIM_CULVER]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_PETE_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.ASHCAN_PETE]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_MARK_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.MARK_HARRIGAN]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_MINH_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.MINH_THI_PHAN]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_SEFINA_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.SEFINA_ROUSSEAU]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_AKACHI_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.AKACHI_ONYELE]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_WILLIAM_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.WILLIAM_YORRICK]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_LOLA_INUSE, globalVariables
                .InvestigatorsInUse[Investigator.LOLA_HAYES]);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_ROUGAROU_STATUS, globalVariables.Rougarou);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_STRANGE_SOLUTION, globalVariables.StrangeSolution);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_ARCHAIC_GLYPHS, globalVariables.ArchaicGlyphs);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_CARNEVALE_STATUS, globalVariables.Carnevale);
        campaignValues.put(ArkhamContract.CampaignEntry.COLUMN_CARNEVALE_REWARDS, globalVariables.CarnevaleReward);
        String campaignSelection = ArkhamContract.CampaignEntry._ID + " LIKE ?";
        String[] campaignSelectionArgs = {Long.toString(globalVariables.CampaignID)};
        db.update(
                ArkhamContract.CampaignEntry.TABLE_NAME,
                campaignValues,
                campaignSelection,
                campaignSelectionArgs);

        // Update all night variables
        if (globalVariables.CurrentCampaign == 1) {
            ContentValues nightValues = new ContentValues();
            nightValues.put(ArkhamContract.NightEntry.COLUMN_HOUSE_STANDING, globalVariables.HouseStanding);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_GHOUL_PRIEST, globalVariables.GhoulPriest);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_LITA_STATUS, globalVariables.LitaChantler);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_CULTISTS_INTERROGATED, globalVariables
                    .CultistsInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_MIDNIGHT_STATUS, globalVariables.PastMidnight);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_DREW_INTERROGATED, globalVariables.DrewInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_HERMAN_INTERROGATED, globalVariables
                    .HermanInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_PETER_INTERROGATED, globalVariables.PeterInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_RUTH_INTERROGATED, globalVariables.RuthInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_VICTORIA_INTERROGATED, globalVariables
                    .VictoriaInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_MASKED_INTERROGATED, globalVariables
                    .MaskedInterrogated);
            nightValues.put(ArkhamContract.NightEntry.COLUMN_UMORDHOTH_STATUS, globalVariables.Umordhoth);

            String nightSelection = ArkhamContract.NightEntry.PARENT_ID + " LIKE ?";
            String[] nightSelectionArgs = {Long.toString(globalVariables.CampaignID)};
            db.update(
                    ArkhamContract.NightEntry.TABLE_NAME,
                    nightValues,
                    nightSelection,
                    nightSelectionArgs);
        }

        // Update all Dunwich variables
        if (globalVariables.CurrentCampaign == 2) {
            ContentValues dunwichValues = new ContentValues();
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_FIRST_SCENARIO, globalVariables.FirstScenario);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_INVESTIGATORS_UNCONSCIOUS, globalVariables
                    .InvestigatorsUnconscious);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_HENRY_ARMITAGE, globalVariables.HenryArmitage);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_WARREN_RICE, globalVariables.WarrenRice);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_STUDENTS, globalVariables.Students);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_OBANNION_GANG, globalVariables.ObannionGang);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_FRANCIS_MORGAN, globalVariables.FrancisMorgan);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_INVESTIGATORS_CHEATED, globalVariables
                    .InvestigatorsCheated);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_NECRONOMICON, globalVariables.Necronomicon);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_ADAM_LYNCH_HAROLD_WALSTED, globalVariables
                    .AdamLynchHaroldWalsted);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_DELAYED, globalVariables.InvestigatorsDelayed);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_SILAS_BISHOP, globalVariables.SilasBishop);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_ZEBULON_WHATELEY, globalVariables.ZebulonWhateley);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_EARL_SAWYER, globalVariables.EarlSawyer);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_ALLY_SACRIFICED, globalVariables.AllySacrificed);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_TOWNSFOLK, globalVariables.TownsfolkAction);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_BROOD_ESCAPED, globalVariables.BroodsEscaped);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_INVESTIGATORS_GATE, globalVariables
                    .InvestigatorsGate);
            dunwichValues.put(ArkhamContract.DunwichEntry.COLUMN_YOG_SOTHOTH, globalVariables.YogSothoth);

            String dunwichSelection = ArkhamContract.DunwichEntry.PARENT_ID + " LIKE ?";
            String[] dunwichSelectionArgs = {Long.toString(globalVariables.CampaignID)};
            db.update(
                    ArkhamContract.DunwichEntry.TABLE_NAME,
                    dunwichValues,
                    dunwichSelection,
                    dunwichSelectionArgs);
        }

        // Update investigator entries
        String[] selectionArgs = {Long.toString(globalVariables.CampaignID)};
        String investigatorSelection = ArkhamContract.InvestigatorEntry.PARENT_ID + " = ?";
        db.delete(ArkhamContract.InvestigatorEntry.TABLE_NAME, investigatorSelection,
                selectionArgs);
        ContentValues investigatorValues = new ContentValues();
        for (int i = 0; i < globalVariables.Investigators.size(); i++) {
            investigatorValues.put(ArkhamContract.InvestigatorEntry.PARENT_ID, globalVariables.CampaignID);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.INVESTIGATOR_ID, i);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_NAME, globalVariables
                    .Investigators.get(i).Name);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_STATUS, globalVariables
                    .Investigators.get(i).Status);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_DAMAGE, globalVariables
                    .Investigators.get(i).Damage);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_HORROR, globalVariables
                    .Investigators.get(i).Horror);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_XP, globalVariables
                    .Investigators.get(i).AvailableXP);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_PLAYER, globalVariables
                    .Investigators.get(i).PlayerName);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_DECKNAME, globalVariables
                    .Investigators.get(i).DeckName);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_DECKLIST, globalVariables
                    .Investigators.get(i).Decklist);
            db.insert(ArkhamContract.InvestigatorEntry.TABLE_NAME, null, investigatorValues);
        }
        for (int i = 0; i < globalVariables.SavedInvestigators.size(); i++) {
            investigatorValues.put(ArkhamContract.InvestigatorEntry.PARENT_ID, globalVariables.CampaignID);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.INVESTIGATOR_ID, i + 100);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_NAME, globalVariables
                    .SavedInvestigators.get(i).Name);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_STATUS, globalVariables
                    .SavedInvestigators.get(i).Status);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_DAMAGE, globalVariables
                    .SavedInvestigators.get(i).Damage);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_HORROR, globalVariables
                    .SavedInvestigators.get(i).Horror);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_XP, globalVariables
                    .SavedInvestigators.get(i).AvailableXP);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_PLAYER, globalVariables
                    .SavedInvestigators.get(i).PlayerName);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_DECKNAME, globalVariables
                    .SavedInvestigators.get(i).DeckName);
            investigatorValues.put(ArkhamContract.InvestigatorEntry.COLUMN_INVESTIGATOR_DECKLIST, globalVariables
                    .SavedInvestigators.get(i).Decklist);
            db.insert(ArkhamContract.InvestigatorEntry.TABLE_NAME, null, investigatorValues);
        }
    }

    public static class PlayerCardsDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater and inflate the view
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.c_dialog_player_cards, null);

            Typeface arnopro = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arnopro.otf");
            final CheckBox solution = (CheckBox) v.findViewById(R.id.strange_solution);
            final CheckBox glyphs = (CheckBox) v.findViewById(R.id.archaic_glyphs);
            solution.setTypeface(arnopro);
            glyphs.setTypeface(arnopro);

            Typeface teutonic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/teutonic.ttf");
            TextView title = (TextView) v.findViewById(R.id.player_cards);
            Button cancelButton = (Button) v.findViewById(R.id.cancel_button);
            Button okayButton = (Button) v.findViewById(R.id.okay_button);
            title.setTypeface(teutonic);
            cancelButton.setTypeface(teutonic);
            okayButton.setTypeface(teutonic);

            if (globalVariables.StrangeSolution == 1) {
                solution.setChecked(true);
            }
            if (globalVariables.ArchaicGlyphs == 1) {
                glyphs.setChecked(true);
            }

            okayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (solution.isChecked()) {
                        globalVariables.StrangeSolution = 1;
                    } else {
                        globalVariables.StrangeSolution = 0;
                    }
                    if (glyphs.isChecked()) {
                        globalVariables.ArchaicGlyphs = 1;
                    } else {
                        globalVariables.ArchaicGlyphs = 0;
                    }
                    dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            builder.setView(v);
            return builder.create();
        }

    }

    public static class WeaknessDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater and inflate the view
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.c_dialog_weaknesses, null);

            Typeface arnopro = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arnopro.otf");
            final CheckBox investigatorOne = (CheckBox) v.findViewById(R.id.investigator_one_weakness);
            final CheckBox investigatorTwo = (CheckBox) v.findViewById(R.id.investigator_two_weakness);
            final CheckBox investigatorThree = (CheckBox) v.findViewById(R.id.investigator_three_weakness);
            final CheckBox investigatorFour = (CheckBox) v.findViewById(R.id.investigator_four_weakness);
            investigatorOne.setTypeface(arnopro);
            investigatorTwo.setTypeface(arnopro);
            investigatorThree.setTypeface(arnopro);
            investigatorFour.setTypeface(arnopro);

            Typeface teutonic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/teutonic.ttf");
            TextView title = (TextView) v.findViewById(R.id.investigator_weaknesses);
            Button cancelButton = (Button) v.findViewById(R.id.cancel_button);
            Button okayButton = (Button) v.findViewById(R.id.okay_button);
            title.setTypeface(teutonic);
            cancelButton.setTypeface(teutonic);
            okayButton.setTypeface(teutonic);

            switch(globalVariables.Investigators.size()){
                case 4:
                    if (globalVariables.Investigators.get(3).TempWeakness == 1) {
                        investigatorFour.setChecked(true);
                    }
                case 3:
                    if (globalVariables.Investigators.get(2).TempWeakness == 1) {
                        investigatorThree.setChecked(true);
                    }
                case 2:
                    if (globalVariables.Investigators.get(1).TempWeakness == 1) {
                        investigatorTwo.setChecked(true);
                    }
                case 1:
                    if (globalVariables.Investigators.get(0).TempWeakness == 1) {
                        investigatorOne.setChecked(true);
                    }
                    break;
            }

            for (int i = 0; i < globalVariables.Investigators.size(); i++) {
                CheckBox box = investigatorOne;
                switch (i) {
                    case 0:
                        box = investigatorOne;
                        break;
                    case 1:
                        box = investigatorTwo;
                        break;
                    case 2:
                        box = investigatorThree;
                        break;
                    case 3:
                        box = investigatorFour;
                        break;
                }
                switch (globalVariables.Investigators.get(i).Name) {
                    case Investigator.ROLAND_BANKS:
                        box.setVisibility(VISIBLE);
                        box.setText(R.string.cover_up);
                        break;
                    case Investigator.SKIDS_OTOOLE:
                        box.setVisibility(VISIBLE);
                        box.setText(R.string.hospital_debts);
                        break;
                    case Investigator.ZOEY_SAMARAS:
                        box.setVisibility(VISIBLE);
                        box.setText(R.string.smite_the_wicked);
                        break;
                    case Investigator.JENNY_BARNES:
                        box.setVisibility(VISIBLE);
                        box.setText(R.string.searching_for_izzie);
                        break;
                    case Investigator.AKACHI_ONYELE:
                        box.setVisibility(VISIBLE);
                        box.setText(R.string.angered_spirits);
                        break;
                }
            }


            okayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch(globalVariables.Investigators.size()){
                        case 4:
                            if (investigatorFour.isChecked()) {
                                globalVariables.Investigators.get(3).TempWeakness = 1;
                            } else {
                                globalVariables.Investigators.get(3).TempWeakness = 0;
                            }
                        case 3:
                            if (investigatorThree.isChecked()) {
                                globalVariables.Investigators.get(2).TempWeakness = 1;
                            } else {
                                globalVariables.Investigators.get(2).TempWeakness = 0;
                            }
                        case 2:
                            if (investigatorTwo.isChecked()) {
                                globalVariables.Investigators.get(1).TempWeakness = 1;
                            } else {
                                globalVariables.Investigators.get(1).TempWeakness = 0;
                            }
                        case 1:
                            if (investigatorOne.isChecked()) {
                                globalVariables.Investigators.get(0).TempWeakness = 1;
                            } else {
                                globalVariables.Investigators.get(0).TempWeakness = 0;
                            }
                            break;
                    }

                    dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            builder.setView(v);
            return builder.create();
        }

    }
}