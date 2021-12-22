package me.trade.vivace;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TradeCommand implements CommandExecutor {
    public final static String tradeCommand = "거래";
    public static Map<String, Player> inviteCodes = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals(tradeCommand)) return true;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "유저 전용 커맨드입니다.");
            return true;
        }

        if (args.length < 1) return false;

        Player commandSender = (Player) sender;
        String extraCommmand = args[0];

        if (extraCommmand.equals("신청")) {
            if (args.length < 2) {
                logMessage(commandSender, ChatColor.RED + "유저의 이름을 입력해 주세요.");
                return true;
            }

            Player victim = Bukkit.getPlayer(args[1]);;

            if (victim == null) {
                logMessage(commandSender, ChatColor.RED + "그런 이름의 유저가 없습니다.");
                return true;
            }

            if (commandSender.equals(victim)) {
                logMessage(commandSender, ChatColor.RED + "자기 자신과 거래하지 마세요.");
                return true;
            }

            if (inviteCodes.containsValue(commandSender)) {
                logMessage(commandSender, ChatColor.RED + "이미 신청 중인 거래가 있습니다.");
                return true;
            }

            for (TradeInventoryManager manager : TradeInventoryHandler.tradeInventory) {
                if (manager.p1.equals(commandSender) ||
                        manager.p2.equals(commandSender)) {
                    logMessage(commandSender, ChatColor.RED + "이미 거래 중인 창이 있습니다.\n"
                            + ChatColor.GRAY + "/거래 창" + ChatColor.RED + "으로 진행 중인 거래를 끝마쳐주세요.");
                    return true;
                } else if (manager.p1.equals(victim) ||
                        manager.p2.equals(victim)) {
                    logMessage(commandSender, ChatColor.RED + "이미 다른 유저와 거래 중인 플레이어입니다.\n");
                    return true;
                }
            }

            commandSender.sendTitle(ChatColor.GOLD + "[거래]",
                    ChatColor.YELLOW + victim.getName() + ChatColor.WHITE + "님께 거래를 신청하였습니다.",
                    20, 100, 20);

            victim.sendTitle(ChatColor.GOLD + "[거래]",
                    ChatColor.YELLOW + commandSender.getName() + ChatColor.WHITE + "님에게 거래 신청이 왔습니다.",
                    20, 100, 20);

            logMessage(commandSender, ChatColor.YELLOW + victim.getName() +
                    ChatColor.GRAY + "님께 거래를 신청하였습니다.");
            logMessage(victim, ChatColor.YELLOW + commandSender.getName() +
                    ChatColor.GRAY + "님에게 거래 신청이 왔습니다.");

            TextComponent acceptMessage, rejectMessage;
            String inviteCode = RandomStringUtils.randomAlphanumeric(6);

            acceptMessage = new TextComponent(ChatColor.GREEN + "수락" + ChatColor.WHITE);
            acceptMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/거래 수락 " + inviteCode));
            acceptMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.YELLOW + "클릭하면 수락합니다.").create()));


            rejectMessage = new TextComponent(ChatColor.RED + "거절" + ChatColor.WHITE);
            rejectMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/거래 거절 " + inviteCode));
            rejectMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.YELLOW + "클릭하면 거절합니다.").create()));
            
            victim.spigot().sendMessage(new TextComponent("< "), acceptMessage,
                    new TextComponent(" | "), rejectMessage, new TextComponent(" >"));

            inviteCodes.put(inviteCode, commandSender);

            return true;
        }

        if (extraCommmand.equals("수락")) {
            if (args.length < 2) {
                return true;
            }

            String inviteCode = args[1];

            if (!inviteCodes.containsKey(inviteCode)) {
                logMessage(commandSender, ChatColor.RED + "거절된 신청이거나 만료된 신청입니다.");
                return true;
            }

            Player inviter = inviteCodes.get(inviteCode);

            logMessage(inviter, ChatColor.YELLOW + commandSender.getName() +
                    ChatColor.GREEN + "님이 거래를 수락했습니다.");
            logMessage(commandSender, ChatColor.YELLOW + inviter.getName() +
                    ChatColor.GRAY + "님의 거래를 수락했습니다.");

            inviteCodes.remove(inviteCode);

            TradeInventoryHandler.tradeInventory.add(new TradeInventoryManager(inviter, commandSender));
            
            return true;
        } else if (extraCommmand.equals("거절")) {
            if (args.length < 2) {
                return true;
            }

            String inviteCode = args[1];

            if (!inviteCodes.containsKey(inviteCode)) {
                logMessage(commandSender, ChatColor.RED + "이미 거절된 신청이거나 만료된 신청입니다.");
                return true;
            }

            Player inviter = inviteCodes.get(inviteCode);

            logMessage(inviter, ChatColor.YELLOW + commandSender.getName() +
                    ChatColor.RED + "님이 거래를 거절했습니다.");
            logMessage(commandSender, ChatColor.YELLOW + inviter.getName() +
                    ChatColor.GRAY + "님의 거래를 거절했습니다.");

            inviteCodes.remove(inviteCode);
            
            return true;
        } else if (extraCommmand.equals("창")) {
            for (TradeInventoryManager manager : TradeInventoryHandler.tradeInventory) {
                if (manager.p1.equals(commandSender) || manager.p2.equals(commandSender)) {
                    commandSender.closeInventory();
                    commandSender.openInventory(manager.tradeInventory);

                    return true;
                }
            }

            logMessage(commandSender, ChatColor.RED + "거래 중인 플레이어가 없습니다.");
            return true;
        }

        return false;
    }

    public void logMessage(Player p, String message) {
        p.sendMessage(ChatColor.GOLD + "[ 거래 ] " + ChatColor.WHITE + message);
    }
}
