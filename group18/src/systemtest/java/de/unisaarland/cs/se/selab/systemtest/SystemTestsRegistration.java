package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.BiddingRoundRobinTest;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.BiddingWithOutEvalTest;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.CompleteBiddingPhase;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.CorrectBiddingEvaluationOption125HCP;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.CorrectBiddingEvaluationOption146AndAdventurerHCP;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.EvaluationFoodFor3PlayersHCP;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.FullBiddingTest;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.NormalBiddingTest;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.PlaceDungeonTilesMax;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.PlaceDungeonTilesMax2;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.PlayerSendsInvalidActionTest;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.PlayerSendsInvalidActionTest2;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.PlayerSendsInvalidActionTest3;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.PlayerSendsInvalidActionTest4;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.TunnelTest01HCP;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.TunnelTest02HCP;
import de.unisaarland.cs.se.selab.systemtest.biddingtest.TunnelTest03HCP;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test121;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test122;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test123;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test124;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test125;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test125InvalidActions;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test127;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test128;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test129;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test130;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test131;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test132;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test133;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test134;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test135;
import de.unisaarland.cs.se.selab.systemtest.completegame.Test137;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigTooFewEverythingTest;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigTooFewRoomsTest;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigUnuniqueAdventurerIdsTest;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigUnuniqueMonsterIdsTest;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigUnuniqueRoomIdsTest;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigUnuniqueTrapIdsTest;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigValidTest1;
import de.unisaarland.cs.se.selab.systemtest.configtest.ConfigValidTest2;
import de.unisaarland.cs.se.selab.systemtest.configtest.EmptyConfigTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.AllPlayerLeaveDuringRegistrationHCP;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.MaxPlayerRegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.MaxPlayerStartGameTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.PlayerLeavesEarlyHCP;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.PlayerStartsGameTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.registrationtest.TwoPlayersStartGameTest;
import de.unisaarland.cs.se.selab.systemtest.singleplayertets.SinglePlayerRoomsTestFPY;
import de.unisaarland.cs.se.selab.systemtest.singleplayertets.SinglePlayerRoomsTestHCP;
import de.unisaarland.cs.se.selab.systemtest.singleplayertets.SinglePlayerSingleYear1FPY;
import de.unisaarland.cs.se.selab.systemtest.singleplayertets.TwoPlayerEdgeCaseChasingFPY;
import de.unisaarland.cs.se.selab.systemtest.specifictest.MoreThan4Players;

final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    /*private static void completeGameHelp1(final SystemTestManager manager) {
        manager.registerTest(new Test1());
        manager.registerTest(new Test2());
        manager.registerTest(new Test3());
        manager.registerTest(new Test4());
        manager.registerTest(new Test5());
        manager.registerTest(new Test6());
        manager.registerTest(new Test7());
        manager.registerTest(new Test8());
        manager.registerTest(new Test9());
        manager.registerTest(new Test10());
        manager.registerTest(new Test11());
        manager.registerTest(new Test12());
        manager.registerTest(new Test13());
        manager.registerTest(new Test14());
        manager.registerTest(new Test15());
        manager.registerTest(new Test16());
        manager.registerTest(new Test17());
        manager.registerTest(new Test18());
        manager.registerTest(new Test19());
        manager.registerTest(new Test20());
        manager.registerTest(new Test21());
        manager.registerTest(new Test22());
        manager.registerTest(new Test23());
        manager.registerTest(new Test24());
        manager.registerTest(new Test25());
        manager.registerTest(new Test26());
        manager.registerTest(new Test27());
        manager.registerTest(new Test28());
        manager.registerTest(new Test29());
        manager.registerTest(new Test30());
        manager.registerTest(new Test31());
        manager.registerTest(new Test32());
        manager.registerTest(new Test33());
        manager.registerTest(new Test34());
        manager.registerTest(new Test35());
        manager.registerTest(new Test36());
        manager.registerTest(new Test37());
        manager.registerTest(new Test38());
        manager.registerTest(new Test39());
        manager.registerTest(new Test40());
        manager.registerTest(new Test41());
        manager.registerTest(new Test42());
        manager.registerTest(new Test43());
        manager.registerTest(new Test44());
        manager.registerTest(new Test45());
        manager.registerTest(new Test46());
        manager.registerTest(new Test47());
        manager.registerTest(new Test48());
        manager.registerTest(new Test49());
        manager.registerTest(new Test50());
        manager.registerTest(new Test51());
        manager.registerTest(new Test52());
        manager.registerTest(new Test53());
        manager.registerTest(new Test54());
        manager.registerTest(new Test55());
        manager.registerTest(new Test56());
        manager.registerTest(new Test57());
        manager.registerTest(new Test58());
        manager.registerTest(new Test59());
        manager.registerTest(new Test60());
        manager.registerTest(new Test61());
        manager.registerTest(new Test62());
        manager.registerTest(new Test63());
        manager.registerTest(new Test64());
        manager.registerTest(new Test65());
        manager.registerTest(new Test66());
        manager.registerTest(new Test67());
        manager.registerTest(new Test68());
        manager.registerTest(new Test69());
        manager.registerTest(new Test70());
        manager.registerTest(new Test71());
        manager.registerTest(new Test72());
        manager.registerTest(new Test73());
        manager.registerTest(new Test74());
        manager.registerTest(new Test75());
        manager.registerTest(new Test76());
        manager.registerTest(new Test77());
        manager.registerTest(new Test78());
        manager.registerTest(new Test79());
        manager.registerTest(new Test80());
        manager.registerTest(new Test81());
        manager.registerTest(new Test82());
        manager.registerTest(new Test83());
        manager.registerTest(new Test84());
        manager.registerTest(new Test85());
        manager.registerTest(new Test86());
        manager.registerTest(new Test87());
        manager.registerTest(new Test88());
        manager.registerTest(new Test89());
        manager.registerTest(new Test90());
    }*/

    private static void completeGameHelp2(final SystemTestManager manager) {
        /*manager.registerTest(new Test91());
        manager.registerTest(new Test92());
        manager.registerTest(new Test93());
        manager.registerTest(new Test94());
        manager.registerTest(new Test95());
        manager.registerTest(new Test96());
        manager.registerTest(new Test97());
        manager.registerTest(new Test98());
        manager.registerTest(new Test99());
        manager.registerTest(new Test100());
        manager.registerTest(new Test101());
        manager.registerTest(new Test102());
        manager.registerTest(new Test103());
        manager.registerTest(new Test104());
        manager.registerTest(new Test105());
        manager.registerTest(new Test106());
        manager.registerTest(new Test107());
        manager.registerTest(new Test108());
        manager.registerTest(new Test109());
        manager.registerTest(new Test110());
        manager.registerTest(new Test111());
        manager.registerTest(new Test112());
        manager.registerTest(new Test113());
        manager.registerTest(new Test114());
        manager.registerTest(new Test115());
        manager.registerTest(new Test116());
        manager.registerTest(new Test117());
        manager.registerTest(new Test118());
        manager.registerTest(new Test119());
        manager.registerTest(new Test120());*/
        manager.registerTest(new Test121());
        manager.registerTest(new Test122());
        manager.registerTest(new Test123());
        manager.registerTest(new Test124());
        manager.registerTest(new Test125());
        //manager.registerTest(new Test125InvalidActions());
        manager.registerTest(new Test127());
        manager.registerTest(new Test128());
        manager.registerTest(new Test129());
        manager.registerTest(new Test130());
        manager.registerTest(new Test131());
        manager.registerTest(new Test132());
        manager.registerTest(new Test133());
        manager.registerTest(new Test134());
        manager.registerTest(new Test135());
        //manager.registerTest(new Test136());
        manager.registerTest(new Test137());
    }

    private static void completeGame(final SystemTestManager manager) {
        // completeGameHelp1(manager);
        completeGameHelp2(manager);
    }

    private static void biddingTests(final SystemTestManager manager) {
        manager.registerTest(new TunnelTest01HCP());
        manager.registerTest(new TunnelTest02HCP());
        manager.registerTest(new TunnelTest03HCP());
        manager.registerTest(new CompleteBiddingPhase());
        manager.registerTest(new FullBiddingTest());
        manager.registerTest(new BiddingRoundRobinTest());
        manager.registerTest(new BiddingWithOutEvalTest());
        manager.registerTest(new CorrectBiddingEvaluationOption125HCP());
        manager.registerTest(new CorrectBiddingEvaluationOption146AndAdventurerHCP());
        manager.registerTest(new EvaluationFoodFor3PlayersHCP());
        manager.registerTest(new NormalBiddingTest());
        // manager.registerTest(new P2HiresP1sMonster());
        manager.registerTest(new PlayerSendsInvalidActionTest());
        manager.registerTest(new PlayerSendsInvalidActionTest2());
        manager.registerTest(new PlayerSendsInvalidActionTest3());
        manager.registerTest(new PlayerSendsInvalidActionTest4());

        manager.registerTest(new MoreThan4Players());
    }

    private static void configTests(final SystemTestManager manager) {

        manager.registerTest(new ConfigTooFewEverythingTest());
        manager.registerTest(new ConfigTooFewRoomsTest());
        manager.registerTest(new ConfigUnuniqueAdventurerIdsTest());
        manager.registerTest(new ConfigUnuniqueMonsterIdsTest());
        manager.registerTest(new ConfigUnuniqueRoomIdsTest());
        manager.registerTest(new ConfigUnuniqueTrapIdsTest());
        manager.registerTest(new ConfigValidTest1());
        manager.registerTest(new ConfigValidTest2());
        manager.registerTest(new EmptyConfigTest());

    }

    private static void registrationTests(final SystemTestManager manager) {

        manager.registerTest(new AllPlayerLeaveDuringRegistrationHCP());
        manager.registerTest(new MaxPlayerRegistrationTest());
        manager.registerTest(new MaxPlayerStartGameTest());
        manager.registerTest(new PlayerLeavesEarlyHCP());
        manager.registerTest(new PlayerStartsGameTest());
        manager.registerTest(new RegistrationTest());
        manager.registerTest(new TwoPlayersStartGameTest());

    }

    private static void singlePlayer(final SystemTestManager manager) {

        //manager.registerTest(new SinglePlayerByMax());
        manager.registerTest(new PlaceDungeonTilesMax());
        manager.registerTest(new PlaceDungeonTilesMax2());
        manager.registerTest(new SinglePlayerSingleYear1FPY());
        manager.registerTest(new SinglePlayerRoomsTestFPY());
        manager.registerTest(new TwoPlayerEdgeCaseChasingFPY());
        manager.registerTest(new SinglePlayerRoomsTestHCP());

    }

    static void registerSystemTests(final SystemTestManager manager) {
        biddingTests(manager);
        configTests(manager);
        registrationTests(manager);
        singlePlayer((manager));
        completeGame(manager);
        manager.registerTest(new Test125InvalidActions());
    }
}
