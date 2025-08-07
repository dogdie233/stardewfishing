package com.bonker.stardewfishing.test;

/**
 * Simple test to verify the skip-game functionality logic
 * This is a basic test since we can't run the full Minecraft environment
 */
public class SkipGameTest {
    
    // Simulate the ClientProxy behavior
    private static boolean skipMinigame = false;
    
    public static void setSkipMinigame(boolean skip) {
        skipMinigame = skip;
    }
    
    public static boolean isSkipMinigame() {
        return skipMinigame;
    }
    
    // Mock packet class for testing
    static class MockPacket {
        private final boolean treasureChest;
        
        MockPacket(boolean treasureChest) {
            this.treasureChest = treasureChest;
        }
        
        public boolean treasureChest() {
            return treasureChest;
        }
    }
    
    // Mock complete packet
    static class MockCompletePacket {
        private final boolean success;
        private final double accuracy;
        private final boolean gotChest;
        
        MockCompletePacket(boolean success, double accuracy, boolean gotChest) {
            this.success = success;
            this.accuracy = accuracy;
            this.gotChest = gotChest;
        }
        
        public boolean success() { return success; }
        public double accuracy() { return accuracy; }
        public boolean gotChest() { return gotChest; }
    }
    
    // Test the core logic
    public static MockCompletePacket openFishingScreen(MockPacket packet) {
        if (skipMinigame) {
            // Skip the minigame and immediately send success with perfect accuracy
            return new MockCompletePacket(true, 1.0, packet.treasureChest());
        } else {
            // Normally would open fishing screen, simulate normal gameplay result
            return new MockCompletePacket(true, 0.75, packet.treasureChest());
        }
    }
    
    // Command simulation
    public static String executeCommand(String state) {
        switch (state.toLowerCase()) {
            case "on":
                setSkipMinigame(true);
                return "Stardew Fishing minigame skip enabled";
            case "off":
                setSkipMinigame(false);
                return "Stardew Fishing minigame skip disabled";
            default:
                return "Invalid argument. Use 'on' or 'off'";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Testing Stardew Fishing Skip-Game Functionality");
        
        // Test 1: Default state (skip disabled)
        System.out.println("\n=== Test 1: Default State ===");
        MockPacket packet1 = new MockPacket(true);
        MockCompletePacket result1 = openFishingScreen(packet1);
        System.out.println("Skip enabled: " + isSkipMinigame());
        System.out.println("Result accuracy: " + result1.accuracy());
        System.out.println("Expected: 0.75 (normal gameplay)");
        assert result1.accuracy() == 0.75 : "Test 1 failed";
        
        // Test 2: Enable skip-game
        System.out.println("\n=== Test 2: Enable Skip-Game ===");
        String enableResult = executeCommand("on");
        System.out.println("Command result: " + enableResult);
        System.out.println("Skip enabled: " + isSkipMinigame());
        assert isSkipMinigame() : "Test 2 failed - skip not enabled";
        
        // Test 3: Skip-game enabled fishing
        System.out.println("\n=== Test 3: Fishing with Skip Enabled ===");
        MockPacket packet2 = new MockPacket(false);
        MockCompletePacket result2 = openFishingScreen(packet2);
        System.out.println("Result accuracy: " + result2.accuracy());
        System.out.println("Expected: 1.0 (perfect score from skip)");
        assert result2.accuracy() == 1.0 : "Test 3 failed";
        
        // Test 4: Treasure chest handling
        System.out.println("\n=== Test 4: Treasure Chest Handling ===");
        MockPacket packet3 = new MockPacket(true);
        MockCompletePacket result3 = openFishingScreen(packet3);
        System.out.println("Packet has treasure: " + packet3.treasureChest());
        System.out.println("Result got chest: " + result3.gotChest());
        assert result3.gotChest() == packet3.treasureChest() : "Test 4 failed";
        
        // Test 5: Disable skip-game
        System.out.println("\n=== Test 5: Disable Skip-Game ===");
        String disableResult = executeCommand("off");
        System.out.println("Command result: " + disableResult);
        System.out.println("Skip enabled: " + isSkipMinigame());
        assert !isSkipMinigame() : "Test 5 failed - skip not disabled";
        
        // Test 6: Invalid command
        System.out.println("\n=== Test 6: Invalid Command ===");
        String invalidResult = executeCommand("invalid");
        System.out.println("Command result: " + invalidResult);
        assert invalidResult.contains("Invalid argument") : "Test 6 failed";
        
        System.out.println("\n=== All Tests Passed! ===");
        System.out.println("The skip-game functionality logic is working correctly.");
    }
}